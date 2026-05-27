# GitHub Actions 自动构建 APK 指南

## 📋 概述

本项目已配置 GitHub Actions，可以自动构建 Android APK 并发布到 GitHub Releases。

## 🚀 快速开始

### 方式一：自动构建（推荐）

1. **将代码推送到 GitHub**
   ```bash
   cd d:\个人记账
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/你的用户名/你的仓库名.git
   git push -u origin main
   ```

2. **自动触发构建**
   - 推送代码到 `main` 或 `develop` 分支会自动触发构建
   - 在 GitHub 仓库页面点击 "Actions" 标签查看构建进度

3. **下载 APK**
   - 构建完成后，进入 Actions 页面
   - 点击对应的构建任务
   - 在页面底部找到 "Artifacts" 区域
   - 下载 `debug-apk` 或 `release-apk`

### 方式二：手动触发构建

1. 进入 GitHub 仓库的 "Actions" 页面
2. 选择 "Build Debug APK" 工作流
3. 点击 "Run workflow" 按钮
4. 选择分支并点击 "Run workflow"

## 📦 工作流说明

### 1. Build Debug APK

**触发条件：**
- 推送代码到 `main` 或 `develop` 分支
- 创建 Pull Request
- 手动触发

**输出：**
- `app-debug.apk` - Debug 版本 APK

**保留时间：** 30 天

### 2. Release APK

**触发条件：**
- 推送版本标签（如 `v1.0.0`）
- 手动触发并指定版本号

**输出：**
- `app-debug.apk` - Debug 版本
- `app-release-unsigned.apk` - 未签名的 Release 版本
- `app-release.apk` - 已签名的 Release 版本（如配置了签名）

**发布位置：** GitHub Releases

## 🔐 配置签名（可选）

如需生成已签名的 Release APK，需要在 GitHub 仓库中配置以下 Secrets：

### 1. 生成签名密钥

```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
```

### 2. 配置 GitHub Secrets

进入 GitHub 仓库 → Settings → Secrets and variables → Actions → New repository secret

添加以下密钥：

| Secret 名称 | 说明 |
|------------|------|
| `KEYSTORE_FILE` | Base64 编码的 keystore 文件内容 |
| `KEYSTORE_PASSWORD` | Keystore 密码 |
| `KEY_PASSWORD` | 密钥密码 |
| `KEY_ALIAS` | 密钥别名 |

### 3. 生成 Base64 编码的 keystore

```bash
# Windows PowerShell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("my-release-key.jks"))

# Linux/Mac
base64 my-release-key.jks
```

## 📱 安装 APK

### 方法一：直接安装

1. 从 GitHub 下载 APK 文件
2. 传输到手机（USB、微信、邮件等）
3. 在手机上点击 APK 文件
4. 如提示"允许安装未知来源应用"，在设置中开启

### 方法二：通过 ADB 安装

```bash
# 连接手机并开启 USB 调试
adb install app-debug.apk

# 启动应用
adb shell am start -n com.example.pfinance/.MainActivity
```

## 🔄 版本发布流程

1. **更新版本号**
   - 编辑 [app/build.gradle.kts](app/build.gradle.kts)
   - 修改 `versionName` 和 `versionCode`

2. **提交并推送**
   ```bash
   git add .
   git commit -m "Release v1.0.0"
   git push origin main
   ```

3. **创建标签**
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

4. **自动发布**
   - GitHub Actions 会自动构建并创建 Release
   - 在 Releases 页面下载 APK

## ⚙️ 自定义配置

### 修改触发分支

编辑 [.github/workflows/build-apk.yml](.github/workflows/build-apk.yml)：

```yaml
on:
  push:
    branches:
      - main
      - develop
      - your-branch  # 添加你的分支
```

### 修改构建类型

在构建步骤中添加或修改 Gradle 任务：

```yaml
- name: Build APK
  run: ./gradlew assembleDebug assembleRelease
```

### 添加测试步骤

```yaml
- name: Run Tests
  run: ./gradlew test

- name: Run Instrumented Tests
  uses: reactivecircus/android-emulator-runner@v2
  with:
    api-level: 34
    script: ./gradlew connectedAndroidTest
```

## 🐛 常见问题

### Q1: 构建失败提示 "Gradle wrapper not found"

**解决方案：** 项目缺少 Gradle Wrapper 文件，需要生成：

```bash
# 在项目根目录运行
gradle wrapper --gradle-version 8.5
```

### Q2: 构建时间过长

**解决方案：** 启用 Gradle 缓存（已默认配置）

### Q3: 下载的 APK 无法安装

**解决方案：**
- 确保手机 Android 版本 ≥ 8.0
- 开启"允许安装未知来源应用"
- 尝试使用 Debug 版本 APK

### Q4: 如何查看构建日志

**解决方案：**
- 进入 GitHub 仓库 → Actions
- 点击对应的构建任务
- 查看详细日志输出

## 📊 构建状态徽章

在 README.md 中添加构建状态徽章：

```markdown
![Build Status](https://github.com/你的用户名/你的仓库名/workflows/Build%20Debug%20APK/badge.svg)
```

## 🎯 最佳实践

1. **定期更新依赖**：保持 Gradle 和依赖库版本最新
2. **使用语义化版本**：遵循 `v主版本.次版本.修订号` 格式
3. **编写测试**：确保代码质量
4. **保护主分支**：设置分支保护规则
5. **使用环境变量**：敏感信息使用 GitHub Secrets

## 📚 相关资源

- [GitHub Actions 文档](https://docs.github.com/en/actions)
- [Android Gradle 插件文档](https://developer.android.com/studio/build)
- [Gradle Wrapper 文档](https://docs.gradle.org/current/userguide/gradle_wrapper.html)