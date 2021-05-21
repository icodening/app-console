package cn.icodening.console.extension;


import cn.icodening.console.Sortable;

import java.util.List;

/**
 * 扩展点加载完毕的后置处理器
 *
 * @author icodening
 * @date 2021.01.04
 */
@Extensible
public interface ExtensionLoadedPostProcessor extends Sortable {

    /**
     * 扩展点加载完毕后调用
     *
     * @param extensionDefinitions 当前加载完毕的扩展点包装对象集合
     * @param extensionLoader   当前加载完毕的扩展点加载器
     */
    void postLoaded(List<ExtensionDefinition<?>> extensionDefinitions, ExtensionLoader<?> extensionLoader);

}
