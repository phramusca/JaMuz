# Robot Framework Testing #

[[TOC]]

## Setup ##

install python, ...

### robotframework / RemoteSwingLibrary plugin ###

[RemoteSwingLibrary homepage](https://github.com/robotframework/remoteswinglibrary)

Plugin swinglibrary-2.3.0.jar fails with Python 3.12.0 ([see post on forum](https://forum.robotframework.org/t/error-in-library-remoteswinglibrary-adding-keyword/6125/3): "getargspec has been removed in 3.11 alpha 2").

So we need to use an older python version that still include getargspec.

Since we only need this python version for this use case, we will install it only for local `SwingLibrary` folder with help of pyenv:

- Install pyenv

  ```bash
  curl https://pyenv.run | bash
  ```

  - [Set up your shell environment for Pyenv](https://github.com/pyenv/pyenv#set-up-your-shell-environment-for-pyenv)

- Install python 3.10.5 (latest 3.10.x version available in pyenv)

  ```bash
  pyenv install 3.10.5
  ```

- Setup this python version for `SwingLibrary` folder.

  ```bash
  cd robot
  pyenv local 3.10.5
  ```

- Check python version

  ```bash
  python --version
  ```

- Install robotframework (for this python pyenv):

  ```bash
  pip install robotframework
  ```

### Visual code integration ###

- Install `Robot Framework Language Server` extension

- In order to have syntax highlighting for `RemoteSwingLibrary`, you need to add RemoteSwingLibrary*.jar to pythonpath in `Robot Framework Language Server` extension settings:

  ```json
  {
      "robot.pythonpath": [
          "/path/to/repo/JaMuz/robot/Lib/remoteswinglibrary-2.3.2.jar"
      ]
  }
  ```

### Github self-hosted runner ###

- [Create a new self-hosted runner](https://github.com/phramusca/JaMuz/settings/actions/runners/new) on WSL or directly on linux

- Install dependencies:
  - (If required): Install Custom SSL Certificate

    ```bash
    sudo cp /mnt/c/IT/netskope/rootcacert.pem /usr/local/share/ca-certificates/
    sudo update-ca-certificates
    ```

  - Install maven

    ```bash
    sudo apt install maven
    ```

  - Install sqlite3

    ```bash
    sudo apt install sqlite3
    ```

- Start the runner

  ```bash
  ./run.sh
  ```


## Swing explorer / JSpy ##

In order to determine identifiers for your test, you can use either Swing explorer or JSpy.

[Tools to help component discovery](https://github.com/robotframework/SwingLibrary/wiki/Getting-Started#tools-to-help-component-discovery)

### JSpy ###

[JSpy](https://code.google.com/archive/p/robotframework-javatools/wikis/JSpy.wiki) is an ugly but yet useful tool to get identifiers.

> On Windows, I did not manage to directly launch a java program (java -jar or jav -cp) directly in File/Launch. However, if you place your command line in a batch, and use that batch, it works (ex: launch.bat).

> On Linux, I tried sh ./launch.sh or directly java - jar /foo/bar.jar, but nothing seems to work.

Ex: from the demo app, when you mouse over the input text field, you will get (among others) this information : ``Name- description`` so you will use "description" as identifier for this button.

```robot
Insert Into Text Field    description    buy milk
```

### Swing explorer ###

This tool is more advanced (you can click on tabs and buttons to open further panels and so on for ex.) and will display more information.

- Linux

```bash
java -cp swexpl.jar:swag.jar:/path/to/actions-runner/_work/JaMuz/JaMuz/dist/JaMuz.jar org.swingexplorer.Launcher jamuz.Main
```

- Windows ( !! A VERIFIER !!)

```bat
java -cp swexpl.jar;swag.jar;\\path\\to\\actions-runner\\_work\\JaMuz\\JaMuz\\dist\\JaMuz.jar org.swingexplorer.Launcher jamuz.Main
```

## Run ##

```bash
robot --outputdir logs --pythonpath Lib/remoteswinglibrary-2.3.2.jar .
```
