package cn.icodening.console.intercept;

import cn.icodening.console.AgentInitializer;
import cn.icodening.console.util.ExtensionClassLoaderHolder;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author icodening
 * @date 2021.08.21
 */
public class AgentMethodInterceptInitializer implements AgentInitializer {

    @Override
    public void initialize(String agentArgs, Instrumentation instrumentation) {
        List<InterceptorDefine> interceptorDefines = new ArrayList<>();
        ServiceLoader<InterceptorDefine> load = ServiceLoader.load(InterceptorDefine.class, ExtensionClassLoaderHolder.get());
        Iterator<InterceptorDefine> iterator = load.iterator();
        while (iterator.hasNext()) {
            interceptorDefines.add(iterator.next());
        }
        if (interceptorDefines == null || interceptorDefines.isEmpty()) {
            return;
        }

        AgentBuilder.Default agentBuilder = new AgentBuilder.Default(new ByteBuddy());

        //reduce type matcher
        ElementMatcher.Junction<NamedElement> typeMatcher = interceptorDefines.stream()
                .flatMap(define -> Stream.of(ElementMatchers.named(define.type())))
                .reduce(ElementMatchers.any(), ElementMatcher.Junction::or);

        Map<String, List<InterceptorDefine>> interceptorDefineMap = interceptorDefines.stream().collect(Collectors.groupingBy(InterceptorDefine::type));

        agentBuilder.type(typeMatcher)
                .transform(new AgentBuilder.Transformer() {
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
                        List<InterceptorDefine> interceptorDefines = interceptorDefineMap.get(typeDescription.getTypeName());
                        for (InterceptorDefine interceptorDefine : interceptorDefines) {
                            InterceptPoint[] interceptPoints = interceptorDefine.getInterceptPoints();
                            List<InstanceMethodInterceptor> inters = new ArrayList<>();
                            DynamicType.Builder.MethodDefinition.ImplementationDefinition<?> method = null;
                            for (InterceptPoint interceptPoint : interceptPoints) {
                                ElementMatcher<MethodDescription> methodsMatcher = interceptPoint.getMethodsMatcher();
                                inters.add(interceptPoint.getMethodInterceptor());
                                method = builder.method(methodsMatcher);
                            }
                            if (method != null && !inters.isEmpty()) {
                                InstanceMethodAroundInterceptor instanceMethodAroundInterceptor = new InstanceMethodAroundInterceptor(inters);
                                builder = method.intercept(MethodDelegation.withDefaultConfiguration()
                                        .withBinders(Morph.Binder.install(OverrideCallable.class))
                                        .to(instanceMethodAroundInterceptor));
                            }
                        }
                        return builder;
                    }
                }).installOn(instrumentation);


    }
}
