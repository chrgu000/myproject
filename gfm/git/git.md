## 设置SSH KEY
- GitHub 上连接已有仓库时的认证，是通过使用了 SSH 的公开密钥认证方式进行的。现在让我们来创建公开密钥认证所需的 SSH Key，并将其添加至 GitHub。已经创建过的读者，请用现有的密钥进行设置。
	- 运行下面的命令创建 SSH Key。
		```
		$ ssh-keygen -t rsa -C "your_email@example.com"
		Generating public/private rsa key pair.
		Enter file in which to save the key
		(/Users/your_user_directory/.ssh/id_rsa):  按回车键
		Enter passphrase (empty for no passphrase):  输入密码
		Enter same passphrase again:  再次输入密码
		```
	- “your_email@example.com”的部分请改成您在创建账户时用的邮箱地址。密码需要在认证时输入，请选择复杂度高并且容易记忆的组合。
	- 输入密码后会出现以下结果。
		```
		Your identification has been saved in /Users/your_user_directory/.ssh/id_rsa.
		Your public key has been saved in /Users/your_user_directory/.ssh/id_rsa.pub.
		The key fingerprint is:
		fingerprint值  your_email@example.com
		The key's randomart image is:
		+---[RSA 2048]----+
		|.    .+E=o       |
		|o.o.++o++=       |
		|.=.*oo oB .      |
		|==*.+ =o *       |
		|Boo= * .S o      |
		|o.+ +    .       |
		|   o             |
		|                 |
		|                 |
		+----[SHA256]-----+
		```
	- id_rsa 文件是私有密钥，id_rsa.pub 是公开密钥。
- 添加公开密钥，在 GitHub 中添加公开密钥，今后就可以用私有密钥进行认证了。
	- 点击右上角的账户设定按钮（Account Settings） ，选择 SSH and GPG Keys 菜单。点击 new SSH Key 之后，会出现如图3.2 的输入框。在 Title 中输入适当的密钥名称。Key 部分请粘贴 id_rsa.pub 文件里的内容。id_rsa.pub的内容可以用如下方法查看。
		```
		$ cat ~/.ssh/id_rsa.pub
		ssh-rsa  公开密钥的内容  your_email@example.com
		```
	- 添加成功之后，创建账户时所用的邮箱会接到一封提示“公共密钥添加完成”的邮件。
- 完成以上设置后，就可以用手中的私人密钥与 GitHub 进行认证和通信了。让我们来实际试一试。
```
$ ssh -T git@github.com
The authenticity of host 'github.com (207.97.227.239)' can't be established.
RSA key fingerprint is  fingerprint值  .
Are you sure you want to continue connecting (yes/no)?  输入yes
Enter passphrase for key '/c/Users/ww/.ssh/id_rsa': (这里需要输入创建SSH Key时设置的密码)
```
- 出现如下结果即为成功。
```
Hi baozc! You've successfully authenticated, but GitHub does not provide shell access.
```
## 公开代码
-  clone 已有仓库，尝试在已有仓库中添加代码并加以公开。首先将已有仓库 clone 到身边的开发环境中。、
	- 仓库的路径，在代码仓库界面选择 Clone or download > Use SSH > 复制对应路径
		```
		λ git clone git@github.com:baozc/test.git
		Cloning into 'test'...
		Enter passphrase for key '/c/Users/ww/.ssh/id_rsa':(这里会要求输入 GitHub 上设置的公开密钥的密码)
		remote: Counting objects: 6, done.
		remote: Compressing objects: 100% (4/4), done.
		remote: Total 6 (delta 0), reused 3 (delta 0), pack-reused 0
		Receiving objects: 100% (6/6), done.
		Checking connectivity... done.
		```
	- 认证成功后，仓库便会被 clone 至仓库名后的目录中。将想要公开的代码提交至这个仓库再 push 到 GitHub 的仓库中，代码便会被公开。
	- 代码默认下载到用户目录下 ~ or C盘 用户 对应用户名文件夹下

## 进行 push
- 提交代码后，只有执行 push，GitHub 上的仓库才会被更新。
```
$ git push
Enter passphrase for key '/c/Users/ww/.ssh/id_rsa':
Counting objects: 3, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (3/3), 349 bytes | 0 bytes/s, done.
Total 3 (delta 0), reused 0 (delta 0)
To github.com:baozc/test.git
   8cc4ff1..6a2c236  master -> master
```
- 这样一来代码就在 GitHub 上公开了。

## git操作命令
- `git init` 初始化仓库，如果初始化成功，执行了 `git init`命令的目录下就会生成 .git 目录。这个 .git 目录里存储着管理当前目录内容所需的仓库数据。
- `git status` 查看仓库的状态，工作树和仓库在被操作的过程中，状态会不断发生变化。在 Git 操作过程中时常用 `git status`命令查看当前状态，可谓基本中的基本。
- `git add` 向暂存区中添加文件，如果只是用 Git 仓库的工作树创建了文件，那么该文件并不会被记入 Git 仓库的版本管理对象当中。要想让文件成为 Git 仓库的管理对象，就需要用 git add命令将其加入暂存区（Stage 或者 Index）中。暂存区是提交之前的一个临时区域。
	- `git add xx`命令可以将xx文件或目录添加到暂存区
	- `git add -A .`来一次添加所有改变的文件。注意 -A 选项后面还有一个句点. `git add -A`表示添加所有内容
	- `git add .` 表示添加新文件和编辑过的文件不包括删除的文件
	- `git add -u` 表示添加编辑或者删除的文件，不包括新添加的文件。
- `git commit` 保存仓库的历史记录
	+ 格式 `git commit -m description`，例 `git commit -m "提交描述"`。**windows下，请注意使用双引号**，否则可能出现错误：`error: pathspec 'commit'' did not match any file(s) known to git.`
	+ 如果想要记述得更加详细，请不加 -m，直接执行 `git commit`命令。执行后编辑器就会启动。在编辑器中记述提交信息的格式如下：
		- 第一行：用一行文字简述提交的更改内容
		- 第二行：空行
		- 第三行以后：记述更改的原因和详细内容
		- 将提交信息按格式记述完毕后，请保存并关闭编辑器，以 #（井号）标为注释的行不必删除。随后，刚才记述的提交信息就会被提交。
		- **中止提交** 如果在编辑器启动后想中止提交，请将提交信息留空并直接关闭编辑器，随后提交就会被中止。
	+ 只要按照上面的格式输入，今后便可以通过确认日志的命令或工具看到这些记录。
	+ 在以 #（井号）标为注释的 Changes to be committed（要提交的更改）栏中，可以查看本次提交中包含的文件。
- `git log` 查看提交日志，包括可以查看什么人在什么时候进行了提交或合并，以及操作前后有怎样的差别。
	- **只显示提交信息的第一行** 可以在 `git log`命令后加上 `--pretty=short`
	- **只显示指定目录、文件的日志** 只要在 `git log`命令后加上目录名，便会只显示该目录下的日志。如果加的是文件名，就会只显示与该文件相关的日 志。
	- **显示文件的改动** 如果想查看提交所带来的改动，可以加上 **-p** 参数，文件的前后差别就会显示在提交信息之后。
	- **查看更改前后的差别** `git diff`命令可以查看工作树、暂存区之间的差别。*在没把文件加入暂存区时，使用git diff会显示暂存区和工作树之间的差别*
		- 及没把修改文件加入暂存区前，使用`git diff`进行比对，如果已经把修改的文件加入暂存区了，使用`git diff`是无法比对的。
	- **查看工作树和最新提交的差别** `git diff HEAD`查看本次提交与上次提交之间有什么差别, HEAD 是指向当前分支中最新一次提交的指针。
		+ 即指`git add`添加到暂存区的文件和上次提交(工作树)文件的差别
		+ 不妨养成这样一个好习惯：在执行 `git commit`命令之前先执行`git diff HEAD`命令，查看本次提交与上次提交之间有什么差别，等确认完毕后再进行 提交。
	- **比对文件更改前后差别，没添加暂存区时使用`git diff`，添加暂存区后使用`git diff head`**

***

## git分支命令
- `git branch`查看分支。可以看到 master 分支左侧标有“\*”（星号），表示这是我们当前所在的分支
- `git checkout -b`——创建、切换分支
- `git checkout -` 切换回上一个分支，像上面这样用“-”（连字符）代替分支名，就可以切换至上一个分支。
- **特性分支** 顾名思义，是集中实现单一特性（主题），除此之外不进行任何作业的分支。在日常开发中，往往会创建数个特性分支，同时在此之外再保留一个随时可以发布软件的稳定分支。稳定分支的角色通常由 master 分支担当。即便在开发过程中发现了 BUG，也需要再创建新的分支，在新分支中进行修正。
- **主干分支** 是刚才我们讲解的特性分支的原点，同时也是合并的终点。通常人们会用 master 分支作为主干分支。主干分支中并没有开发到一半的代码，可以随时供他人查看。*有时我们需要让这个主干分支总是配置在正式环境中，有时又需要用标签 Tag 等创建版本信息，同时管理多个版本发布。拥有多个版本发布时，主干分支也有多个。*
-  `git merge`——合并分支，为了在历史记录中明确记录下本次分支合并，我们需要创建合并提交。因此，在合并时加上 `--no-ff` 参数。
	- 例：`git merge --no-ff feature-A`，随后编辑器会启动，用于录入合并提交的信息。默认信息中已经包含了是从 feature-A 分支合并过来的相关内容，所以可不必做任何更改。将编辑器中显示的内容保存，关闭编辑器  
- `git log  -- graph` 以图表形式查看分支
