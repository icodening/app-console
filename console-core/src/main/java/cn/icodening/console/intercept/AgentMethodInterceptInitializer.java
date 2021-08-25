package cn.icodening.console.intercept;

import cn.icodening.console.AgentInitializer;
import cn.icodening.console.util.ExtensionClassLoaderHolder;
import cn.icodening.console.util.StringUtil;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 字节码侵入初始化，需要使用侵入功能的模块扩展实现 {@link InterceptorDefine} 接口即可.
 * by {@link java.util.ServiceLoader}
 *
 * @author icodening
 * @date 2021.08.21
 */
public class AgentMethodInterceptInitializer implements AgentInitializer {

    @Override
    public void initialize(String agentArgs, Instrumentation instrumentation) {
        List<InterceptorDefine> interceptorDefines = new ArrayList<>();
        ServiceLoader<InterceptorDefine> load = ServiceLoader.load(InterceptorDefine.class, ExtensionClassLoaderHolder.get());
        if (!load.iterator().hasNext()) {
            return;
        }
        for (InterceptorDefine value : load) {
            interceptorDefines.add(value);
        }

        AgentBuilder.Default agentBuilder = new AgentBuilder.Default(new ByteBuddy());

        //filter available defines
        List<InterceptorDefine> availableInterceptorDefines = interceptorDefines.stream()
                .filter(Objects::nonNull)
                .filter(define -> !StringUtil.isBlank(define.type()))
                .filter(define -> Objects.nonNull(define.getInterceptPoints()))
                .filter(define -> define.getInterceptPoints().length > 0)
                .collect(Collectors.toList());

        //reduce type matcher
        ElementMatcher.Junction<NamedElement> typeMatcher = availableInterceptorDefines.stream()
                .flatMap(define -> Stream.of(ElementMatchers.named(define.type())))
                .reduce(ElementMatchers.none(), ElementMatcher.Junction::or);

        //grouping by type
        Map<String, List<InterceptorDefine>> interceptorDefineMap = availableInterceptorDefines.stream().collect(Collectors.groupingBy(InterceptorDefine::type));

        //FIXME 可以考虑添加拦截器排序功能
        agentBuilder.type(typeMatcher)
                .transform((builder, typeDescription, classLoader, module) -> {
                    List<InterceptorDefine> interceptorDefinesByType = interceptorDefineMap.get(typeDescription.getTypeName());
                    for (InterceptorDefine interceptorDefine : interceptorDefinesByType) {
                        InterceptPoint[] interceptPoints = interceptorDefine.getInterceptPoints();
                        DynamicType.Builder.MethodDefinition.ImplementationDefinition<?> method = null;
                        DynamicType.Builder.MethodDefinition.ImplementationDefinition<?> constructor = null;
                        for (InterceptPoint interceptPoint : interceptPoints) {
                            //constructor
                            ElementMatcher<MethodDescription> constructorMatcher = interceptPoint.getConstructorMatcher();
                            constructor = builder.constructor(constructorMatcher);
                            ConstructorInterceptor constructorInterceptor = interceptPoint.getConstructorInterceptor();
                            if (constructor != null && constructorInterceptor != null) {
                                AfterConstructorInterceptor afterConstructorInterceptor = new AfterConstructorInterceptor(Collections.singletonList(constructorInterceptor));
                                builder = constructor.intercept(SuperMethodCall.INSTANCE.andThen(MethodDelegation.withDefaultConfiguration()
                                        .to(afterConstructorInterceptor)));
                            }

                            //method
                            ElementMatcher<MethodDescription> methodsMatcher = interceptPoint.getMethodsMatcher();
                            method = builder.method(methodsMatcher);
                            InstanceMethodInterceptor methodInterceptor = interceptPoint.getMethodInterceptor();
                            if (method != null && methodInterceptor != null) {
                                InstanceMethodAroundInterceptor instanceMethodAroundInterceptor = new InstanceMethodAroundInterceptor(Collections.singletonList(methodInterceptor));
                                builder = method.intercept(MethodDelegation.withDefaultConfiguration()
                                        .withBinders(Morph.Binder.install(OverrideCallable.class))
                                        .to(instanceMethodAroundInterceptor));
                            }
                        }

                    }
                    return builder;
                }).installOn(instrumentation);


    }
}
