# PersonalFinance - 个人记账应用

一个使用 Kotlin 和 Jetpack Compose 构建的现代化个人财务管理应用。

## ✨ 功能特性

- 💰 交易记录管理（收入/支出）
- 📊 多维度统计分析
- 🎯 预算设置与跟踪
- 💳 多账户管理
- 📈 图表可视化
- 🔒 应用锁保护
- ☁️ 云端同步（Firebase）
- 📤 数据导出（Excel/PDF）

## 📱 下载安装

### 方式一：GitHub Actions 自动构建（推荐）

1. **推送代码到 GitHub**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/你的用户名/你的仓库名.git
   git push -u origin main
   ```

2. **自动构建 APK**
   - 推送代码后自动触发构建
   - 进入仓库的 "Actions" 页面查看构建进度
   - 构建完成后下载 APK 文件

3. **安装到手机**
   - 下载 `debug-apk` 或 `release-apk`
   - 在手机上点击 APK 文件安装

### 方式二：手动触发构建

1. 进入 GitHub 仓库 → Actions
2. 选择 "Build Debug APK" → 点击 "Run workflow"
3. 等待构建完成后下载 APK

详细说明请查看 [GITHUB_ACTIONS_GUIDE.md](GITHUB_ACTIONS_GUIDE.md)

## 🛠️ 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose + Material3
- **架构**: MVVM + Repository Pattern
- **依赖注入**: Hilt
- **数据库**: Room + SQLCipher
- **导航**: Navigation Component
- **图表**: Vico Compose
- **后台**: Firebase (Auth/Firestore/Storage)

## 📦 系统要求

- Android 8.0 (API 26) 或更高版本
- 存储空间: 至少 50MB

## 🚀 本地开发

### 环境要求

- JDK 17+
- Android Studio Hedgehog (2023.1.1) 或更高版本
- Android SDK API 34
- Gradle 8.5+

### 构建步骤

```bash
# 克隆项目
git clone https://github.com/你的用户名/你的仓库名.git
cd 你的仓库名

# 使用 Android Studio 打开项目
# 等待 Gradle 同步完成

# 连接设备或启动模拟器
# 点击运行按钮
```

## 📝 版本发布

1. 更新 [app/build.gradle.kts](app/build.gradle.kts) 中的版本号
2. 提交并推送代码
3. 创建版本标签：
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
4. GitHub Actions 自动构建并发布到 Releases

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

本项目采用 MIT 许可证。

## 📮 联系方式

如有问题或建议，请提交 Issue。