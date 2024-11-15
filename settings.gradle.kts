rootProject.name = "iao"

include("plugin")
include("project:runtime-app")
include("project:module-util")
include("project:module-command")
include("project:module-http")
include("project:module-api")
include("project:module-satoken")
include("project:module-redis")
include("project:module-database")

//include("project:module-test")
include("project:example:example-command")
include("project:example:example-router")
include("project:example:example-data")