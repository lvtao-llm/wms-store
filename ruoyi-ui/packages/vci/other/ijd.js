const fs = require("fs");
const path = require("path");
// 是否未接口联调环境
const isEnvInterfaceJoinDebug = process.env.ijd === "yes";
// 更改调试模式配置
function updateEnvConfig() {
  const pathEnv = path.resolve(__dirname, "../env.js");
  const env = fs.readFileSync(pathEnv).toString().replace(/(false;|true;)/, `${isEnvInterfaceJoinDebug};`);
  fs.writeFileSync(pathEnv, env);
}
updateEnvConfig();
// 是否构建环境
const isEnvProduction = process.env.NODE_ENV === "production";
module.exports = {
  isEnvInterfaceJoinDebug,
  isEnvProduction
};
