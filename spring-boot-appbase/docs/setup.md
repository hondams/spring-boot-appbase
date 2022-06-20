# 環境構築

## chocolatey のインストール

1. 「Windows PowerShell」を管理者モードで起動。
    - Windowsキー＋Sを押下、powershellを入力、「Windows PowerShell」に対して、「管理者として実行する」を選択。

1. 以下のコマンドを実行。

``` powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
```

## chocolatey で、各種ツールのインストール

1. 以下のコマンドを実行。

``` cmd
cinst -y 7zip
cinst -y adobereader
cinst -y AdoptOpenJDK11
cinst -y androidstudio
cinst -y eclipse
cinst -y git
cinst -y googlechrome
cinst -y google-translate-chrome
cinst -y maven
cinst -y vscode
cinst -y graphviz
cinst -y winmerge
cinst -y putty
cinst -y winscp
cinst -y sakuraeditor --params "'/Tasks:fileassoc'"
```

1. graphvizの環境変数登録

``` cmd
setx GRAPHVIZ_DOT=C:\ProgramData\chocolatey\bin\dot.exe
```

## vscode で、各種プラグインのインストール

``` cmd
code --install-extension eamodio.gitlens
code --install-extension donjayamanne.githistory
code --install-extension vscode-icons-team.vscode-icons
code --install-extension davidanson.vscode-markdownlint
code --install-extension yzhang.markdown-all-in-one
code --install-extension shd101wyy.markdown-preview-enhanced
code --install-extension oderwat.indent-rainbow
code --install-extension jebbs.plantuml
code --install-extension hediet.vscode-drawio
code --install-extension christian-kohler.path-intellisense
code --install-extension mosapride.zenkaku
code --install-extension marp-team.marp-vscode
code --install-extension RandomFractalsInc.vscode-vega-viewer
code --install-extension evilz.vscode-reveal
code --install-extension jspolancor.presentationmode
```

## 参考

### chocolatey のインストール（オリジナル）

- [https://chocolatey.org/install](https://chocolatey.org/install)

### chocolatey のコマンド

#### chocolatey のパッケージのインストール

``` cmd
cinst -y <パッケージ名>
```

#### chocolatey のパッケージの最新化

``` cmd
cup -y all
```

#### chocolatey のインストール済みパッケージの確認

``` cmd
clist -lo
```

#### chocolatey のパッケージのオフラインインストール

- 以下に、ダウンロードしたパッケージを格納。

``` path
%TEMP%\chocolatey\<パッケージ名>\<バージョン>\
```

- ダウンロード済みパッケージのインストール

``` cmd
cinst -f <パッケージ名>
```

- 参考URL

https://docs.chocolatey.org/en-us/choco/setup#completely-offline-install


### eclipse plugin
- m2e-apt
- MapStruct
- Spring Tools 4
- m2e
- EclEmma

- 参考URL
	- https://marketplace.eclipse.org/content/mapstruct-eclipse-plugin
	- https://mapstruct.org/documentation/stable/reference/html/#configuration-options
    - https://mapstruct.org/documentation/ide-support/
    - https://stackoverflow.com/questions/65380359/lomboks-access-to-jdk-compilers-internal-packages-incompatible-with-java-16


### VsCode のコマンド

- 拡張機能のインストール
    code --install-extension <拡張機能ID>
- インストール済み拡張機能の確認
    code --list-extensions
