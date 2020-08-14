EE-122 Network Simulator
------------------------


文件形式
-----------
模拟器组织如下：
simulator.py - 开启模拟器
sim/core.py - 模拟器内部工作。小心别碰（ Keep out）.
sim/api.py - 你所用到的模拟器的一部分(如Entity类).  参见帮助(api).
sim/basics.py - 使用API构建的基本模拟器,如Hub Entity。参见帮助(basics).
sim/topo.py - 可以使用它来构建你自己的拓扑/场景。参见帮助(topo).
scenarios/*.py - 你可以用于测试的拓扑和场景。参见帮助(scenarios).
tests/*.py - 自动测试案例示例，包括兼容性测试。


现在开始
---------------
运行simulator.py开启模拟器:

    $ python simulator.py

这将输出一些信息，并提供一个Python解释器。通过解释器,你可以运行任何Python代码,查看并操作模拟,并且可以得到有关模拟器多方面的帮助。

在simulator.py启动后,会简单地创建一个测试场景(基本上这就是一个拓扑) 并开启模拟器。你可能希望修改它来加载一个不同的场景(包括一个自己设计的)。特别是,你可以修改22行的场景，及15行在网络中使用的交换机。缺省时，模拟器会装入"linear"场景（两台主机通过单个HUB相连）及Hub交换机，该交换机仅在两者之间转发。如果你想改变场景,需要重启simulator.py.

为了改变由模拟器引入的场景和交换机，找到定义交换机（switch）和场景(scenario)的项，并编辑他们，如：
from learning_switch import LearningSwitch as switch
from rip_router import RIPRouter as switch
import scenarios.linear as scenario import scenarios.candy as scenario
你可以查看hub.py中用于Hub交换机的源代码，以及目录scenarios中场景的源代码。

scenario目录中的每个场景都包含一个create()方法。你可能需要仔细看看这些，他们创建场景的拓扑图。所附的场景可以在任意交换机类（switch class）中运行,因此你可以容易地使用hub, 学习交换机, 你的类RIP交换机等来建立它们

从模拟器的命令行,你能访问在你的场景中建立的任何实体（Entities），并且可以与它们交互。如:

    >>> start()
    >>> h1.ping(h2)

开始时，建议大家玩一下缺省的场景和交换机，并确保你知道如何在交换机间使用ping,在log中添加调试（DEBUG）消息，并且理解缺省输出到log viewer的DEBUG消息。

Log Viewer
---------------------
Log消息通过是发送到所运行模拟器的终端。如果你使用交互解释器或做实验，这会有点讨厌。可以通过编辑simulator.py来禁用，在顶部有这样一行：#DISABLE CONSOLE_LOG ＝ True

如果不注释这一行（通过删除'#')，则在终端将看不到log消息。当然，这些log消息可能非常有用，而你也可能很想看到他们。为了能做到，有一个独立的log viewer：logviewer.py.将其与模拟器一起运行如下：
$python logviewer.py & python simulator.py
运行此程序将打开一个新的窗口，其中你将可以看到模拟器中的主机和交换机输出的调试信息。

实现实体（Entities）
---------------------
存在于模拟器中的对象是超类Entity的子类(在api.py中)。有多个应用函数, 同时也有一些空函数，当各种事件发生时这些函数被调用。你可能希望处理其中至少部分事件!更多帮助可以在模拟器中尝试help(api.Entity)


发送包
---------------
实体可以收发包。在此模拟器中，这即意味着包Packet的子类。作为一个例子可以查看basics.Ping,查看basics.BasicHost可以看到如何使用的例子。在缺省的模拟器中（带有HUB的线性拓扑），尝试h1.ping(h2)开始。


构建你自己的场景
---------------------------
可能希望使用你自己的拓扑来进行测试,可能你想在这些拓扑中测试你自己的事件(如结点加入和离开网络).我们把这些称为"场景"。为了让你能够工作，模拟器已经带了一些场景(在场景目录中), 但你可能想构建你自己的。可以如下开始:

    >>> import sim.topo as topo

第一步是简单地创建一些实体（Entities）以便模拟器能使用它们。你不必自己创建结点，而是让在内核（core）中的函数CreateEntity()帮你建立, 如下面的行所示:

    >>> CreateEntity('myNewNode', MyNodeType, arg1, arg2)

即, 你不要使用通常的Python对象创建如:

    >>> x = MyNodeType(arg1, arg2) # 不要这样做

CreateEntity()返回新实体,并且你所建立的所有实体都加到了sim对象中。因此，你可以:

    >>> import sim
    >>> x = CreateEntity('myNewNode', MyNodeType, arg1, arg2)
    >>> print sim.myNewNode, x

.. 这会显示新实体再次。

为了把它与其他实体相连，可做如下事情:

    >>> topo.link(sim.myNewNode, sim.someOtherNode)

也可以取消链路，和与所有东西断开如下:

    >>> topo.disconnect(sim.myNewNode)

为了查看某个实体的连接情况，可以:

    >>> topo.show_ports(sim.someNode)

.. 这会显示someNode上的结点如何与其他结点及其端口相连。


编写测试用例
-------------
由于你可以创建自己的定制拓扑，这使得能非常容易地编写自己的测试用例;我们并未为本作业的每个环境提供"测试平台"。我们确实提供了一个测试情形，其与实际打分的非常相似，以便让你知道你如何对你提交的作业自动测试。你可以在命令行提示下运行测试如下(不是在simulator.py中):

    $ python tests/<test case>.py

非常重要: 在测试文件夹中，我们也提供了一个独立的"兼容测试"(tests/compat_test.py)。在你提交前，请确保你的程序通过此测试，以使我们能够给你的程序评分。没有通过此测试，你将得零分!


可视化
-------------
最后模拟器包括一个图形用户接口，对其的支持严重不足。但如果你想玩一下的话，拿出来玩一下。NetVis是一个用于网络模拟器的可视化工具。其基于Processing编写，Processing是一个JAVA中编写可视化工具的框架（with some nice shortcuts).

你应该已经接收到可在LINUX，MAC OS和WINDOWS下运行的程序。假定你已经安装了JAVA（这对MAC总是成立的，但对另外两个则不一定），很有希望能直接运行。

如果在运行GUI时遇到问题，最可能的解释是你没有安装JAVA。可以试着装一下。替代的方案是，你可以安装Processing(可以直接从processing.org下载)并复制NetVis的workbook目录到Processing项目文件夹。

你模拟器中的实体，链路和包能自动显示在NetVis中。一般来说，这些东西会自动摆放。你也可以把他们订在某处，通过点击附带的泡泡。你可以缩放，也可以使用“adjustments”panal中的选项改变其样式（点击它来打开）


