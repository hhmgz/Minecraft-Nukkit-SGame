package cn.hhm.mc.game.base.module.plugin

import cn.nukkit.plugin.PluginDescription

class ModuleDescription(yamlMap: Map<String, Any>) : PluginDescription(yamlMap) {
    /**
     * 返回这个插件的SPluginBase适用版本。<br></br>
     */
    var baseVersion: String? = null
        private set
    var type: String? = null
        private set
    var key: String? = null
        private set
    var authorizationMode: String? = null
        private set

    init {
        if (yamlMap.containsKey("forBase")) {
            this.baseVersion = yamlMap["forBase"] as String
        }
        if (yamlMap.containsKey("type")) {
            this.type = yamlMap["type"] as String
        }
        if (yamlMap.containsKey("key")) {
            this.key = yamlMap["key"] as String
        }
        if (yamlMap.containsKey("authorizationMode")) {
            this.authorizationMode = yamlMap["authorizationMode"] as String
        }
    }
}
