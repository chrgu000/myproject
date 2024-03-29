## 与档案系统及程序的限制关系： ulimit
>想象一个状况：我的 Linux 主机里面同时登入了十个人，这十个人不知怎么搞的， 同时开启了 100 个档案，每个档案的大小约 10MBytes ，请问一下， 我的 Linux 主机的内存要有多大才够？ 10*100*10 = 10000MBytes ～～ 老天爷，这样，系统不挂点才有鬼哩！为了要预防这个情况的发生，所以， 我们的 bash 是可以『限制使用者的某些系统资源』的，包括可以开启的档案数量， 可以使用的 CPU 时间，可以使用的内存总量等等。如何设定？用 ulimit 吧！

```
[root@linux ~]# ulimit [-SHacdflmnpstuv] [配额]
参数：
-H ：hard limit ，严格的设定，必定不能超过设定的值；
-S ：soft limit ，警告的设定，可以超过这个设定值，但是会有警告讯息，
并且，还是无法超过 hard limit 的喔！也就是说，假设我的 soft limit
为 80 ， hard limit 为 100 ，那么我的某个资源可以用到 90 ，
可以超过 80 ，还是无法超过 100 ，而且在 80~90 之间，会有警告讯息的意思。
-a ：列出所有的限制额度；
-c ：可建立的最大核心档案容量 (core files)
-d ：程序数据可使用的最大容量
-f ：此 shell 可以建立的最大档案容量 (一般可能设定为 2GB)单位为 Kbytes
-l ：可用于锁定 (lock) 的内存量
-p ：可用以管线处理 (pipe) 的数量
-t ：可使用的最大 CPU 时间 (单位为秒)
-u ：单一使用者可以使用的最大程序(process)数量。
范例：
范例一：列出所有的限制数据
[root@linux ~]# ulimit -a
范例二：限制使用者仅能建立 1MBytes 以下的容量的档案
[root@linux ~]# ulimit -f 1024
```

还记得我们在 Linux 磁盘档案系统 里面提到过，单一 filesystem 能够支持的单一档案大小与 block 的大小有关。例如 block size 为 1024 byte 时，单一档案可达 16GB 的容量。但是，我们可以用 ulimit 来限制使用者可以建立的档案大小喔！ 利用 ulimit -f 就可以来设定了！例如上面的范例二，要注意单位喔！单位是 Kbytes。 若改天你一直无法建立一个大容量的档案，记得瞧一瞧 ulimit 的信息喔！( 不过，要注意的是，一般身份使用者如果以 ulimit 设定了 -f 的档案大小， 那么他『只能减小档案大小，不能增加档案大小喔！』)
