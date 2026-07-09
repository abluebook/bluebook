# 页面样式与默认文案修改说明

本文记录蓝书页面风格、初始化公告、站点文案和页脚信息常见修改位置。

## 蓝书设计范式

### 配色

- 品牌主色：`#1E80FF`，用于主按钮、选中导航、选中标签、链接和交互高亮。
- 品牌深蓝：`#2563EB`，用于 Hover 或强调状态。
- 页面背景：`#FFFFFF`。
- 浅灰背景：`#F4F4F4` / `#F8F9FA`，用于搜索框、非激活按钮、浅色容器。
- Hover 背景：`#F0F0F0`。
- 主文本：`#333333`。
- 次文本：`#999999`。

### 布局

- PC 端采用左侧固定导航 + 右侧内容区的布局思路。
- 侧边栏宽度参考 `240px`，导航项用 Flex 垂直排列，图标与文字水平垂直居中。
- 顶部 Header 高度参考 `72px`，搜索框居中。
- 内容卡片使用多列瀑布流或弹性网格，列间距/行间距保持 `16px-20px`。

### 组件尺寸

- 登录/主按钮：高度 `40px`，圆角 `20px`，背景品牌蓝。
- 搜索框：高度 `40px`，宽度约 `400px`，圆角 `20px`。
- 分类标签：高度约 `32px`，圆角 `16px`。
- 用户头像：`24px × 24px`，圆角 `50%`。
- 卡片圆角：`12px` 或 `16px`，图片上半部分贴合圆角。

### 动效

- 卡片 Hover：上移 `2px` 或增加轻阴影 `0 4px 12px rgba(0, 0, 0, 0.08)`。
- 过渡时间：`0.2s`。
- 非激活图标/文字可降低透明度到 `0.6`。
- Active 状态可使用 `opacity: 0.8`。

### 实施文件

样式源文件在 SCSS 目录，优先修改 SCSS，再通过 `npm run build` 生成 CSS：

```text
src/main/resources/scss/base.scss
src/main/resources/scss/index.scss
src/main/resources/scss/mobile-base.scss
src/main/resources/scss/responsive.scss
src/main/resources/scss/_variables.scss
src/main/resources/scss/_common.scss
```

构建后输出到：

```text
src/main/resources/css/base.css
src/main/resources/css/index.css
src/main/resources/css/mobile-base.css
src/main/resources/css/responsive.css
```

其中：

- `base.scss` / `base.css`：PC 端公共样式、导航、按钮、模块、列表。
- `index.scss` / `index.css`：首页、文章页、登录注册引导页相关样式。
- `mobile-base.scss` / `mobile-base.css`：移动端公共样式。
- `responsive.scss` / `responsive.css`：响应式适配。

PC 端导航、页头、页脚、首页模板优先修改：

```text
src/main/resources/skins/classic/header.ftl
src/main/resources/skins/classic/footer.ftl
src/main/resources/skins/classic/index.ftl
```

移动端对应修改：

```text
src/main/resources/skins/mobile/header.ftl
src/main/resources/skins/mobile/footer.ftl
src/main/resources/skins/mobile/index.ftl
```

修改 SCSS 或 JS 后执行：

```bash
npm run build
```

## 初始化公告和默认标签

初始化空库时生成的系统公告、`BlueBook` 标签和欢迎帖在：

```text
src/main/java/org/b3log/symphony/service/InitMgmtService.java
```

常见位置：

```text
src/main/java/org/b3log/symphony/service/InitMgmtService.java:662
src/main/java/org/b3log/symphony/service/InitMgmtService.java:668
src/main/java/org/b3log/symphony/service/InitMgmtService.java:673
src/main/java/org/b3log/symphony/service/InitMgmtService.java:680
src/main/java/org/b3log/symphony/service/InitMgmtService.java:681
src/main/java/org/b3log/symphony/service/InitMgmtService.java:682
```

可修改内容包括：

```java
String tagTitle = Symphonys.SYS_ANNOUNCE_TAG;
tag.put(Tag.TAG_URI, "announcement");

tagTitle = "BlueBook";
tag.put(Tag.TAG_URI, "bluebook");
tag.put(Tag.TAG_DESCRIPTION, "...");

article.put(Article.ARTICLE_TITLE, "欢迎来到散帅的社区 :gift_heart:");
article.put(Article.ARTICLE_TAGS, "系统公告,BlueBook");
article.put(Article.ARTICLE_CONTENT, "这里是蓝书，散帅们的集中营。大家在这里相互信任，以平等 • 自由 • 奔放的价值观进行分享交流。最后请大家共同爱护这个自由的交流环境，哦耶。");
```

注意：这些初始化内容只影响首次初始化的空数据库。已有数据库中的标签、公告文章需要通过后台或数据库修改。

## 默认页面风格

默认皮肤配置在：

```text
src/main/resources/symphony.properties
```

关键配置：

```properties
skinDirName=classic
```

可用皮肤目录在：

```text
src/main/resources/skins/classic
src/main/resources/skins/mobile
```

如需改整体页面结构和样式，优先修改对应 skin 下的模板和样式资源。

## 首页、页头、页脚模板

经典 PC 端模板：

```text
src/main/resources/skins/classic/header.ftl
src/main/resources/skins/classic/footer.ftl
src/main/resources/skins/classic/index.ftl
```

移动端模板：

```text
src/main/resources/skins/mobile/header.ftl
src/main/resources/skins/mobile/footer.ftl
src/main/resources/skins/mobile/index.ftl
```

常见修改点：

- 顶部导航：`header.ftl`
- 首页内容结构：`index.ftl`
- 版权、备案号、版本信息：`footer.ftl`

页脚中类似以下内容在 footer 模板内：

```text
© 2012-present B3log 开源
BlueBook 3.6.4
备案号
```

经典模板位置示例：

```text
src/main/resources/skins/classic/footer.ftl
```

移动端模板位置示例：

```text
src/main/resources/skins/mobile/footer.ftl
```

## 语言包文案

大部分菜单、按钮、提示语来自语言包：

```text
src/main/resources/lang_zh_CN.properties
src/main/resources/lang_en_US.properties
```

例如站点 slogan：

```properties
sloganLabel=Feel easy about trust.
```

位置：

```text
src/main/resources/lang_zh_CN.properties:226
src/main/resources/lang_en_US.properties:226
```

修改菜单或页面入口文案时，可在语言包中搜索：

```text
about
admin
domain
tag
statistic
announcement
sloganLabel
```

对应中文页面可搜索：

```text
关于
管理
领域
标签
数据统计
系统公告
```

## 邮件模板文案

注册、验证码、周报等邮件中的品牌文案在：

```text
src/main/resources/mail_tpl/bluebook_verifycode.ftl
src/main/resources/mail_tpl/bluebook_weekly.ftl
```

例如：

```text
src/main/resources/mail_tpl/bluebook_verifycode.ftl:124
src/main/resources/mail_tpl/bluebook_verifycode.ftl:176
src/main/resources/mail_tpl/bluebook_weekly.ftl:242
```

包含类似文案：

```text
大家在这里相互信任，以 平等 • 自由 • 奔放 的价值观进行分享交流
Feel easy about trust.
```

## 领域、标签、数据统计入口

领域相关路由在：

```text
src/main/java/org/b3log/symphony/processor/Router.java
```

页面模板在 skin 目录下，例如：

```text
src/main/resources/skins/classic/domain-articles.ftl
src/main/resources/skins/mobile/domain-articles.ftl
src/main/resources/skins/classic/tag-articles.ftl
src/main/resources/skins/mobile/tag-articles.ftl
src/main/resources/skins/classic/statistic.ftl
src/main/resources/skins/mobile/statistic.ftl
```

导航名称通常来自语言包，页面结构来自对应 `.ftl` 模板。

## 前端资源构建

顶层 JS 会通过 gulp 生成 `.min.js`：

```text
gulpfile.js
src/main/resources/js/*.js
src/main/resources/js/*.min.js
```

修改 JS 后执行：

```bash
npm run build
```

常见构建输出：

```text
src/main/resources/js/common.min.js
src/main/resources/js/settings.min.js
src/main/resources/js/verify.min.js
src/main/resources/js/lib/compress/libs.min.js
```

## 修改已有数据库内容

初始化代码只在空库首次启动时执行。若数据库已经初始化：

- 默认欢迎帖内容：去后台文章管理修改。
- `系统公告`、`BlueBook` 标签：去后台标签管理修改。
- 站点配置、备案号、描述：去后台配置或数据库 `option` 表修改。

如需通过清库重新初始化，Docker Compose 可执行：

```bash
docker compose down -v
docker compose up -d --build
```

Docker 在 WSL 时：

```bash
wsl docker compose down -v
wsl docker compose up -d --build
```
