# 本地基于 FastGPT 德文检索开发计划

目标：所有组件（FastGPT、MinerU、Mem0、Redis、向量库）全部本地 Docker 化，可离线运行  
原则：每步可 `docker compose up` 一键拉起

## 第一极端｜部署

| 任务                        | 本地操作命令/脚本                                                                                                                                                |
|---------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| 克隆统一仓库                    | `git clone https://github.com/your-org/local-kb-bot.git`                                                                                                 
| 安装 Docker & Nvidia-Docker | `sudo apt install docker-ce docker-compose-plugin nvidia-docker2`                                                                                        
| 本地模型镜像一次性拉好               | `docker pull ghcr.io/huggingface/text-embeddings-inference:1.2-cpu`（bge-m3）<br>`docker pull ghcr.io/huggingface/text-generation-inference:1.4`（可选本地 LLM） 

## 第二阶段|最小可用：本地 FastGPT + 本地 MinerU，解析，入库，对话

|                |
|----------------|
| 本地 MinerU      | |
| 本地向量库 Milvus   | |
| 本地 bge-m3 嵌入服务 | 
| 本地 FastGPT 配置  | 
| 写「本地解析插件」      | 
| 写「本地写入向量」插件    | 
| 建工作流（本地 JSON）  | 

---

## 第三极端｜长期记忆Mem0

| 序号 | 任务             |
|----|----------------|
|    | 启动本地 Mem0      |
|    | 写「Mem0 本地读写」插件 |
|    | 改造工作流          | 

---

## 四｜短期记忆：Redis 缓存最近 10 轮

| 序号 | 本地任务          |
|----|---------------|
| 1  | 启动本地 Redis    |
| 2  | 写「Redis 读写」插件 | 
| 3  | 改造工作流         | 

## 五｜检索质量调优

| 序号 | 本地任务         | 
|----|--------------|
| 1  | 段落切分策略       |
| 2  | 向量模型对比       | 
| 3  | 本地重排序        | 
| 4  | 混合检索         |    
| 5  | Prompt 模板 AB |
