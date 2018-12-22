package cn.hhm.mc.game.base.kernal.plugin

import cn.nukkit.plugin.PluginDescription

class KernelPluginDescription(yamlMap: Map<String, Any>) : PluginDescription(yamlMap) {
    /**
     * 返回这个插件的Kernel适用版本。<br></br>
     * Returns the website of this plugin.
     */
    var kernelVersion: String? = null
        private set

    init {
        if (yamlMap.containsKey("forKernel")) {
            this.kernelVersion = yamlMap["forKernel"] as String
        }
    }
}
