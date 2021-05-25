# wechat

## 1. 项目功能介绍

 ### 1. 1 用户功能

 好友基本功能

①基本功能

1.  用户可通过微信号或用户名添加好友，添加好友需对方通过审核

2. 删除好友，物理删除即可

3. 可对好友进行备注（爱的昵称）

4. 将好友加入黑名单，拉黑后双方不能进行好友间聊天及查看朋友圈

5. 取消拉黑好友

6. 用户可举报好友，需提供举报信息。管理员进行查看并考虑是否封号

7. 用户可发送好友卡片，其他人可通过好友卡片发送好友审核请求

8. 修改用户信息：修改昵称和邮箱和密码

9. 可取消自动登陆1

10. 修改用户头像和聊天背景

11. 可以向超管申请成为管理员，也可以向超管举报管理员

   

   ②聊天功能
   好友聊天

   1. 成为好友后可以互相聊天，加入黑名单后不能进行好友间聊天

   2. 聊天实时刷新。推荐使用webSocket，可了解短连接、长连接、webSocket等的区别

   3. 用户聊天可发送表情包，表情包由系统提供。当然自己也可以上传表情包，注意表情包大小

   4. 好友间可互相传送文件

     群聊

   5. 用户可发起群聊，默认发起群聊用户为群主。群主可修改群名，其他成员不能进行修改

   6. 群主可直接将好友拉入群聊中，无需经过好友同意

   7. 微信用户可通过群号搜索群并申请加入群聊，申请需经过管理员同意

   8. 群主可管理群成员。如禁言、解除禁言以及送飞机票等功能

   9. 群成员可自由退出群聊

   10. 群成员可以修改在群的昵称

   11. 群成员可随意发言，并且发言对所有群成员可见

      8.群主可发布公告，所有群成员会弹出并显示公告的弹窗

   12. 每个用户都可以设置其他用户在群里面的昵称

   13. 用户聊天可发送表情包，表情包由系统提供。当然自己也可以上传表情包，注意表情包大小

   14. 群成员可以发文件

       

       ③朋友圈功能

       1. 用户可以发送朋友圈，默认对好友可见
          										编辑朋友圈富文本实现
                   										朋友圈可以插入多个图片
       2. 点赞、取消点赞
       3. 显示发表时间
       4. 删除朋友圈
       5. 自由设定朋友圈是否对个别好友可见
       6. 评论功能可使用多级评论就是类似于微信朋友圈，相互恢复的两个人有一个单独的对话框，参考csdn
       7. 发布朋友圈时，后台检测朋友圈是否存在敏感词汇，如诋毁党、色情等信息。若存在，则驳回发布朋友圈的请求并提醒发布者

 

 项目管理员功能：

 ①管理员，分为群管理员和微信管理员，微信管理员具有群管所有的功能，但是群管只是管理群
	     1. 好友基本功能，聊天功能，朋友圈功能都和用户一样
		 2. 群管功能
			 2.1群里有多位管理员
			 2. 2管理员可以实现踢人功能
			 2 .3管理员可以发布群公告
			 2 .4管理员可以和群主一样审核申请群的人
	            3. 封号——被封号的用户会接收到封号信息并且只能登录，不能使用其他功能。但可以申请解封，封号——设置账号冻结时间，到期自动解封
	                4. 查看任意朋友圈、删除朋友圈

​					

​	② 项目超管（预设好的）：

​	 超管有的这个都有，另外附加可设置任意人为管理员





### 1.2 sql设计

外键主要是名字的关联和朋友圈id的关联

 #### 1.2.1 全体表

![image-20210523225218954](D:\java笔记\javaweb\wechat.assets\image-20210523225218954.png)

 



#### 1.2.2 每个表介绍

##### 1.2.2.1 ban表（被封号的表）

![image-20210523225404782](D:\java笔记\javaweb\wechat.assets\image-20210523225404782.png)

##### 1.2.2.2 card（好友卡片表）

![image-20210523225440462](D:\java笔记\javaweb\wechat.assets\image-20210523225440462.png)

##### 1.2.2.3 cicle表（朋友圈表，记录每一条发的朋友圈）

![image-20210523225628362](D:\java笔记\javaweb\wechat.assets\image-20210523225628362.png)

##### 1.2.2.4 cicle_comment(用户评论表)

![image-20210523230707822](D:\java笔记\javaweb\wechat.assets\image-20210523230707822.png)

##### 1.2.2.5  cicle_comment_comment(评论中的评论表)

![image-20210523230814571](D:\java笔记\javaweb\wechat.assets\image-20210523230814571.png)



##### 1.2.2.6 cicle_like表：（点赞表）

![image-20210523230755703](D:\java笔记\javaweb\wechat.assets\image-20210523230755703.png)



##### 1.2.2.7 demand_sensitive_word表（敏感词表）

![image-20210523230856819](D:\java笔记\javaweb\wechat.assets\image-20210523230856819.png)

##### 1.2.2.8 group_chat (群聊记录表)

![image-20210523230935812](D:\java笔记\javaweb\wechat.assets\image-20210523230935812.png)



##### 1.2.2.9 group_file （群聊表情包表）

![image-20210523231000355](D:\java笔记\javaweb\wechat.assets\image-20210523231000355.png)





##### 1.2.2.10 groups （群聊表）

![image-20210523231032115](D:\java笔记\javaweb\wechat.assets\image-20210523231032115.png)



##### 1.2.2.11 user (用户表)

![image-20210523231051718](D:\java笔记\javaweb\wechat.assets\image-20210523231051718.png)



##### 1.2.2.12  user_chat（用户聊天记录表）

![image-20210523231111629](D:\java笔记\javaweb\wechat.assets\image-20210523231111629.png)



##### 1.2.2.13 user_friend（用户好友表）

![image-20210523231131012](D:\java笔记\javaweb\wechat.assets\image-20210523231131012.png)



##### 1.2.2.14 user_friend_file（表情包表）

![image-20210523231405258](D:\java笔记\javaweb\wechat.assets\image-20210523231405258.png)



##### 1.2.2.15 user_imformation (用户消息表)

![image-20210523231508128](D:\java笔记\javaweb\wechat.assets\image-20210523231508128.png)



##### 1.2.2.16 user_in_group (用户对群聊的设计表)

![image-20210523231542556](D:\java笔记\javaweb\wechat.assets\image-20210523231542556.png)



### 1.3 jar包

![image-20210523232404790](D:\java笔记\javaweb\wechat.assets\image-20210523232404790.png)

分别为：邮件注册jar包， json配套jar包，gsonjar包， jackson包， 数据库连接包， 延时队列包，jstl包。



 

## 2. 项目功能测试：

### 2.1 用户注册登陆和游客注册登陆

1. 用户注册和游客注册，账号（8-16个字符，含有一个特殊字符，大写字母和小写字母），名字：3-20个字符，密码和账号一样

![image-20210523233722949](D:\java笔记\javaweb\wechat.assets\image-20210523233722949.png)

2. 错误提示:

![image-20210523233819015](D:\java笔记\javaweb\wechat.assets\image-20210523233819015.png)

3. 注册成功提示：

   ![image-20210523233946230](D:\java笔记\javaweb\wechat.assets\image-20210523233946230.png)

4. qq邮箱查看注册

   ![image-20210523234612544](D:\java笔记\javaweb\wechat.assets\image-20210523234612544.png)

   ![image-20210523234626519](D:\java笔记\javaweb\wechat.assets\image-20210523234626519.png)

![image-20210523234640076](D:\java笔记\javaweb\wechat.assets\image-20210523234640076.png)

5. 游客注册和这个一样



### 2.2 用户和游客登陆功能（登陆失败回显账号）

1. 错误提示：账号密码不正确，没激活，被封号了，验证码错误，除了验证码错误，其他的都是显示没找到该用户

<img src="D:\java笔记\javaweb\wechat.assets\image-20210523234910152.png" alt="image-20210523234910152" style="zoom:80%;" />

<img src="D:\java笔记\javaweb\wechat.assets\image-20210523234926608.png" alt="image-20210523234926608" style="zoom:80%;" />

2. 登陆成功

   ![image-20210523235255174](D:\java笔记\javaweb\wechat.assets\image-20210523235255174.png)

3. 自动登陆（勾选后就会保存到cookies中）

   ![image-20210523235609079](D:\java笔记\javaweb\wechat.assets\image-20210523235609079.png)

   再次关闭浏览器打开浏览器就会直接跳转到登陆界面，不需要再次登陆

   ![image-20210523235724413](D:\java笔记\javaweb\wechat.assets\image-20210523235724413.png)

4. 说明：游客和管理员和超管没有自动登陆功能



### 2.3 找回密码

1. 界面

   ![image-20210523235906034](D:\java笔记\javaweb\wechat.assets\image-20210523235906034.png)

   说明：点击退出会返回登陆界面，和注册一样点击重置会清除输入框的所有输入消息



2. 错误提示（账号不匹配，格式错误，邮箱不匹配，格式错误）

   ![image-20210524000337803](D:\java笔记\javaweb\wechat.assets\image-20210524000337803.png)

   ![image-20210524000417334](D:\java笔记\javaweb\wechat.assets\image-20210524000417334.png)





3. 成功提示：发送到邮箱

   ![image-20210524000717664](D:\java笔记\javaweb\wechat.assets\image-20210524000717664.png)

   ![image-20210524002900003](D:\java笔记\javaweb\readme.assets\image-20210524002900003.png)

 

### 2.4 注销功能（取消自动登陆）

![image-20210524004234395](D:\java笔记\javaweb\readme.assets\image-20210524004234395.png)

点击后：cookies清除

![image-20210524004345933](D:\java笔记\javaweb\readme.assets\image-20210524004345933.png)



### 2.5 反馈功能

1. 错误提示：

   ![image-20210524004648386](D:\java笔记\javaweb\readme.assets\image-20210524004648386.png)

   ![image-20210524004707023](D:\java笔记\javaweb\readme.assets\image-20210524004707023.png)

   ![image-20210524004746069](D:\java笔记\javaweb\readme.assets\image-20210524004746069.png)



2. 当没有错误提示的时候就可以提交了

   ![image-20210524004842695](D:\java笔记\javaweb\readme.assets\image-20210524004842695.png)

   结果：已通知对方

   ![image-20210524004904336](D:\java笔记\javaweb\readme.assets\image-20210524004904336.png)

   用另一个谷歌浏览器打开点击左边第一个微信图标可以看见有消息通知

   ![image-20210524005858011](D:\java笔记\javaweb\readme.assets\image-20210524005858011.png)

   管理员收到消息后就可以打开管理员专用的后台界面，进行封号

   

   ### 2.6 设置功能

   说明这里我设置为邮件不可修改

   <img src="D:\java笔记\javaweb\readme.assets\image-20210524010315489.png" alt="image-20210524010315489"  />

   

   1. 错误提示（输入为空，格式不正确，已存在提示）

   ![image-20210524010529283](D:\java笔记\javaweb\readme.assets\image-20210524010529283.png)

   ![image-20210524010609394](D:\java笔记\javaweb\readme.assets\image-20210524010609394.png)

   ![image-20210524010623010](D:\java笔记\javaweb\readme.assets\image-20210524010623010.png)



   2. 设置修改密码

      ![image-20210524010921148](D:\java笔记\javaweb\readme.assets\image-20210524010921148.png)

结果：修改之后此时不可自动登陆了

​			<img src="D:\java笔记\javaweb\readme.assets\image-20210524010948613.png" alt="image-20210524010948613" style="zoom: 50%;" />





3. 修改名字，成功提示

   ![image-20210524012911614](D:\java笔记\javaweb\readme.assets\image-20210524012911614.png)



4. 修改头像(换号测试)

   <img src="D:\java笔记\javaweb\readme.assets\image-20210524014724650.png" alt="image-20210524014724650" style="zoom:50%;" />





### 2.7 搜索添加好友功能（2.6不知道为什么没显示出来，2.6是设置功能）

说明：用户需要点击好友或者群聊才可以进行搜索，否则无效

<img src="D:\java笔记\javaweb\readme.assets\image-20210524083434520.png" alt="image-20210524083434520" style="zoom:67%;" />



1. 错误提示：

   <img src="D:\java笔记\javaweb\readme.assets\image-20210524083614906.png" alt="image-20210524083614906" style="zoom:50%;" />

   ​	<img src="D:\java笔记\javaweb\readme.assets\image-20210524083642939.png" alt="image-20210524083642939" style="zoom:50%;" />



2. **成功提示：四种状态**

   1. 未添加，显示出来添加按钮，可以添加

      ![image-20210524083821323](D:\java笔记\javaweb\readme.assets\image-20210524083821323.png)



​		2. 已添加但是未同意，显示提示信息

​			请求添加者界面

​	   ![image-20210524083923425](D:\java笔记\javaweb\readme.assets\image-20210524083923425.png)

<img src="D:\java笔记\javaweb\readme.assets\image-20210524084022707.png" alt="image-20210524084022707" style="zoom:33%;" />



被添加界面（这里界面是特朗普拜登的界面）(收到请求添加的消息，点击通过就可以进行添加)：

​		![image-20210524084151959](D:\java笔记\javaweb\readme.assets\image-20210524084151959.png)

![image-20210524084302578](D:\java笔记\javaweb\readme.assets\image-20210524084302578.png)





3. 已经是好友了，提示信息

   ![image-20210524084342647](D:\java笔记\javaweb\readme.assets\image-20210524084342647.png)



4. 拉黑提示（对方把你拉黑了就会显示，拉黑不能进行好友聊天）：

   ![image-20210524090106876](D:\java笔记\javaweb\readme.assets\image-20210524090106876.png)







### 2.8 搜索添加群聊功能

1. 错误提示（和好友的基本一样，找不到提示和搜索错误提示）

![image-20210524092315643](D:\java笔记\javaweb\readme.assets\image-20210524092315643.png)



2. 成功提示：（和好友基本一样，未添加状态，成功添加，等待同意）

   

   1. <img src="D:\java笔记\javaweb\readme.assets\image-20210524092514168.png" alt="image-20210524092514168" style="zoom: 50%;" />

      ![image-20210524092658116](D:\java笔记\javaweb\readme.assets\image-20210524092658116.png)



2. ![image-20210524092729991](D:\java笔记\javaweb\readme.assets\image-20210524092729991.png)

3. ![image-20210524092827066](D:\java笔记\javaweb\readme.assets\image-20210524092827066.png)



### 2.9 查看消息提示功能

说明：这一步查看消息主要是当朋友圈被管理员删除或者用户反馈信息或者其他方面就会出现，表示绿色的是已了解，红色是没有了解，如果没有消息就会提示没有消息。对于已了解和删除消息按键，我这里的设计是需要点击左边的消息展示出来后就解除已了解和删除消息的disable属性。

![image-20210524093808350](D:\java笔记\javaweb\readme.assets\image-20210524093808350.png)

![image-20210524093824019](D:\java笔记\javaweb\readme.assets\image-20210524093824019.png)

点击左边的消息框就可以在右边的框内查看消息了

![image-20210524094047668](D:\java笔记\javaweb\readme.assets\image-20210524094047668.png)

点击已了解就可以把相同内容的消息变成绿色，表示了解了。

![image-20210524094205011](D:\java笔记\javaweb\readme.assets\image-20210524094205011.png)

点击删除就可以把相同内容的信息全部删除掉

![image-20210524094311628](D:\java笔记\javaweb\readme.assets\image-20210524094311628.png)

![image-20210524094351292](D:\java笔记\javaweb\readme.assets\image-20210524094351292.png)



### 2.10 好友基础功能

说明：点击左边的第二个按键后就会弹出，点击账号就会弹出好友的详细消息

![image-20210524095440237](D:\java笔记\javaweb\readme.assets\image-20210524095440237.png)

![image-20210524095750301](D:\java笔记\javaweb\readme.assets\image-20210524095750301.png)





1. 拉黑好友，取消拉黑

   ![image-20210524131621739](D:\java笔记\javaweb\readme.assets\image-20210524131621739.png)

   ![image-20210524131637226](D:\java笔记\javaweb\readme.assets\image-20210524131637226.png)





2. 修改备注（好友聊天的时候用到）

   ![image-20210524131754084](D:\java笔记\javaweb\readme.assets\image-20210524131754084.png)



3. 删除好友

   ![image-20210524131845952](D:\java笔记\javaweb\readme.assets\image-20210524131845952.png)

   效果：

   ![image-20210524131932931](D:\java笔记\javaweb\readme.assets\image-20210524131932931.png)



4. 进入聊天室：这个原来是想作为一个页面切换的，但是后来在主页面右边另外加了一个聊天室，所以这里的进入好友聊天室也就不做处理了。



### 2. 11 好友聊天功能（websocket）

![image-20210524132301822](D:\java笔记\javaweb\readme.assets\image-20210524132301822.png)

进入界面：（主页面是和群聊用的同一个css，所以在修改群聊界面的css适合导致了好友界面的css发生错误，在好友界面没有提供更改背景和表情包功能，这些都在群聊中有提供）

![image-20210524132341789](D:\java笔记\javaweb\readme.assets\image-20210524132341789.png)



另开一个谷歌浏览器进行聊天，下面可以看到拉黑的情况下是不可以进行聊天的

![image-20210524132731957](D:\java笔记\javaweb\readme.assets\image-20210524132731957.png)

![image-20210524132757686](D:\java笔记\javaweb\readme.assets\image-20210524132757686.png)



聊天界面：双方的界面，右下角有上线提示功能

![image-20210524133234089](D:\java笔记\javaweb\readme.assets\image-20210524133234089.png)

![image-20210524133248053](D:\java笔记\javaweb\readme.assets\image-20210524133248053.png)

可以看到右下角有上线提示功能，点击名字之后可以开始聊天，上面会显示正在和昵称（真实名字）聊天。昵称就是备注，可以在上面提到的好友基础功能那里设置。



下面演示发送消息，发送图片，发送文件，发送表情

![image-20210524133710548](D:\java笔记\javaweb\readme.assets\image-20210524133710548.png)

![image-20210524133719263](D:\java笔记\javaweb\readme.assets\image-20210524133719263.png)

这边样式做的不好，有些地方出现了很大的偏差，头像消失是因为这里我没有打算将头像传给后台，而是提供人名发送消息。



文件可以下载，点击就可以

![image-20210524133924946](D:\java笔记\javaweb\readme.assets\image-20210524133924946.png)



查看聊天记录和删除聊天记录

![image-20210524134033374](D:\java笔记\javaweb\readme.assets\image-20210524134033374.png)

![image-20210524134147542](D:\java笔记\javaweb\readme.assets\image-20210524134147542.png)

![image-20210524134156447](D:\java笔记\javaweb\readme.assets\image-20210524134156447.png)



### 2.12 查看好友申请并且同意的功能

说明：点击右边的查看申请栏，选择2就可以进入

![image-20210524171627540](D:\java笔记\javaweb\readme.assets\image-20210524171627540.png)

![image-20210524171836883](D:\java笔记\javaweb\readme.assets\image-20210524171836883.png)

点击账号可以查看详细信息，点击通过就可以添加为好友了。

![image-20210524171917555](D:\java笔记\javaweb\readme.assets\image-20210524171917555.png)

结果：

![image-20210524171940759](D:\java笔记\javaweb\readme.assets\image-20210524171940759.png)



### 2.13 好友卡片

![image-20210524172205620](D:\java笔记\javaweb\readme.assets\image-20210524172205620.png)

主界面：

![image-20210524172221907](D:\java笔记\javaweb\readme.assets\image-20210524172221907.png)



1. 错误提示：

   ![image-20210524172328727](D:\java笔记\javaweb\readme.assets\image-20210524172328727.png)

   ![image-20210524173415716](D:\java笔记\javaweb\readme.assets\image-20210524173415716.png)





2. 点击发送卡片就可以发送给对方

   

<img src="D:\java笔记\javaweb\readme.assets\image-20210524173452368.png" alt="image-20210524173452368" style="zoom: 50%;" />



3. 接收人的界面

   ​	 <img src="D:\java笔记\javaweb\readme.assets\image-20210524175447845.png" alt="image-20210524175447845" style="zoom: 33%;" />



4. 先点击名字，就可以点击使用卡片了。使用卡片之后，两个人会添加为好友

   ​	使用后

   ​	<img src="D:\java笔记\javaweb\readme.assets\image-20210524175655909.png" alt="image-20210524175655909" style="zoom:33%;" />

   ​	效果图

   ​	![image-20210524175945281](D:\java笔记\javaweb\readme.assets\image-20210524175945281.png)



### 2.14 查看群聊申请并同意

界面：

​		![image-20210524194700418](D:\java笔记\javaweb\readme.assets\image-20210524194700418.png)

说明：点击就可以显示出来要添加的用户的信息，点击通过就可以同意对方添加，只有管理员和群主有权限操作。如果是普通用户就是空白。

![image-20210524194810340](D:\java笔记\javaweb\readme.assets\image-20210524194810340.png)





点击通过以后

![image-20210524194932003](D:\java笔记\javaweb\readme.assets\image-20210524194932003.png)

结果：

![image-20210524195002166](D:\java笔记\javaweb\readme.assets\image-20210524195002166.png)





### 2.15 创建群聊功能

点击右侧创建群聊，群主自动显示为当前登陆的人

![image-20210524195231931](D:\java笔记\javaweb\readme.assets\image-20210524195231931.png)



1. 错误提示：输入为空，群名或者群账号已存在，群简介含有敏感词，群账号或者群名字不合法，群账号（8-16个字符，含有一个特殊字符，大写字母和小写字母），群名字：3-20个字符

   ![image-20210524195818899](D:\java笔记\javaweb\readme.assets\image-20210524195818899.png)

   ![image-20210524195848882](D:\java笔记\javaweb\readme.assets\image-20210524195848882.png)

   ![image-20210524195948877](D:\java笔记\javaweb\readme.assets\image-20210524195948877.png)

   ![image-20210524200150243](D:\java笔记\javaweb\readme.assets\image-20210524200150243.png)

   这里的敏感词是用数据库中的一个敏感词表来写的

   ![image-20210524200249778](D:\java笔记\javaweb\readme.assets\image-20210524200249778.png)



2. 都没有问题，就可以创建群聊了

   ![image-20210524200516591](D:\java笔记\javaweb\readme.assets\image-20210524200516591.png)

   

   效果：在主界面点击群聊标志后会显示出来新的群

   ![image-20210524200557790](D:\java笔记\javaweb\readme.assets\image-20210524200557790.png)





### 2.16 修改群消息

上图中点击修改信息就可以进入群信息修改界面，界面如下：

![image-20210524201133056](D:\java笔记\javaweb\readme.assets\image-20210524201133056.png)

和用户名字设置一样，如果直接点修改就是保持原来的一样，这里的群账号和群主设置为不可修改了

1. 设置头像：点击浏览，此时设置键取消disable，加入图片就可以设置了

   ![image-20210524202153036](D:\java笔记\javaweb\readme.assets\image-20210524202153036.png)



2. 设置群昵称，群昵称在群聊的时候会用到，和好友的昵称一样，在修改完群昵称之后会刷新页面，此时的群昵称要到群聊那里看

   ![image-20210524204005572](D:\java笔记\javaweb\readme.assets\image-20210524204005572.png)

3. 设置用户在群的昵称，修改后也是在群聊的时候会显示出来

   ![image-20210524204113089](D:\java笔记\javaweb\readme.assets\image-20210524204113089.png)



4. 设置简介，和上面的一样，如果不设置就按原来的显示

   ![image-20210524204307409](D:\java笔记\javaweb\readme.assets\image-20210524204307409.png)

   ![image-20210524204315432](D:\java笔记\javaweb\readme.assets\image-20210524204315432.png)



5. 错误提示：

   ![image-20210524204518992](D:\java笔记\javaweb\readme.assets\image-20210524204518992.png)

   ![image-20210524204546577](D:\java笔记\javaweb\readme.assets\image-20210524204546577.png)

   ![image-20210524204605243](D:\java笔记\javaweb\readme.assets\image-20210524204605243.png)



6. 普通用户只能修改群昵称和在群的昵称，名字和简介都是disabled的

   ![image-20210524204943353](D:\java笔记\javaweb\readme.assets\image-20210524204943353.png)
   



7. 解散群聊：从数据库删除

   ![image-20210525124641418](D:\java笔记\javaweb\readme.assets\image-20210525124641418.png)

   ![image-20210525130705250](D:\java笔记\javaweb\readme.assets\image-20210525130705250.png)   

效果：

​		![image-20210525130718281](D:\java笔记\javaweb\readme.assets\image-20210525130718281.png)





### 2.17 群聊界面功能，websocket实现

说明：群聊功能可以发送消息，表情，文件，图片和表情包

界面：因为没地方放了所以把公告和表情包放在了最下面，系统预设了15个表情包

界面介绍：群聊列表是群聊名字加上群备注，群聊成员是群员名字加上备注

![image-20210524222410648](D:\java笔记\javaweb\readme.assets\image-20210524222410648.png)

![image-20210524222538625](D:\java笔记\javaweb\readme.assets\image-20210524222538625.png)



1. 群聊发送消息、文件、图片、表情(这里的样式弄得不好，功能是实现了，就是展示出来的界面不行)

   发送：

   ![image-20210524230656965](D:\java笔记\javaweb\readme.assets\image-20210524230656965.png)

   接受：

   ![image-20210524230714097](D:\java笔记\javaweb\readme.assets\image-20210524230714097.png)

   ![image-20210524230910134](D:\java笔记\javaweb\readme.assets\image-20210524230910134.png)



2. 查看发送的消息的历史记录：点击查看历史记录就可以了

   ![image-20210524231041772](D:\java笔记\javaweb\readme.assets\image-20210524231041772.png)

   点击清除聊天记录就可以清除所有聊天记录了。





3. 发布公告：输入内容后，点击发送公告就可以了，当然，不是管理员或者群主不可以发布公告

   错误提示：

   ![image-20210524231240444](D:\java笔记\javaweb\readme.assets\image-20210524231240444.png)

   

   发布公告：首先是检测

   ![image-20210524231615035](D:\java笔记\javaweb\readme.assets\image-20210524231615035.png)

   

   发布成功，发布者界面

   ![image-20210524231737425](D:\java笔记\javaweb\readme.assets\image-20210524231737425.png)

   群员界面：群公告提示

   ![image-20210524232134835](D:\java笔记\javaweb\readme.assets\image-20210524232134835.png)

   ![image-20210524232143837](D:\java笔记\javaweb\readme.assets\image-20210524232143837.png)





4. 添加表情包

   ![image-20210524232750759](D:\java笔记\javaweb\readme.assets\image-20210524232750759.png)

   选择这个表情包

   ![image-20210524232816705](D:\java笔记\javaweb\readme.assets\image-20210524232816705.png)

   添加成功显示

   ![image-20210524232906026](D:\java笔记\javaweb\readme.assets\image-20210524232906026.png)

   ​	可以发送表情包

   ![image-20210524233032152](D:\java笔记\javaweb\readme.assets\image-20210524233032152.png)



​		不同的群聊有预设表情包，可以选择自己添加

​		<img src="D:\java笔记\javaweb\readme.assets\image-20210524233228139.png" alt="image-20210524233228139" style="zoom: 33%;" />





5. 对群员进行操作

   ![image-20210524233952420](D:\java笔记\javaweb\readme.assets\image-20210524233952420.png)

   

   5.1 禁言和取消禁言（不是群主和管理员没有这个权限）

   ![image-20210524235902193](D:\java笔记\javaweb\readme.assets\image-20210524235902193.png)

   ![image-20210525000000248](D:\java笔记\javaweb\readme.assets\image-20210525000000248.png)

   

   禁言后，无法发送消息，文件、图片、表情包都不可以：

   ![image-20210525000019368](D:\java笔记\javaweb\readme.assets\image-20210525000019368.png)

   ![image-20210525000056058](D:\java笔记\javaweb\readme.assets\image-20210525000056058.png)

   

   解除禁言后，又可以再次发消息了

   ![image-20210525000315644](D:\java笔记\javaweb\readme.assets\image-20210525000315644.png)

   ![image-20210525000333887](D:\java笔记\javaweb\readme.assets\image-20210525000333887.png)



​	  5.2 设置为群管理员，这一步只有群主才能操作

​		![image-20210525001929532](D:\java笔记\javaweb\readme.assets\image-20210525001929532.png)

​	

设置为管理员后，就可以对别人禁言了。

![image-20210525002249835](D:\java笔记\javaweb\readme.assets\image-20210525002249835.png)

![image-20210525002450871](D:\java笔记\javaweb\readme.assets\image-20210525002450871.png)

![image-20210525002524480](D:\java笔记\javaweb\readme.assets\image-20210525002524480.png)



也可以解除管理员，此时数据库也变回了群员，也就不能禁言了

![image-20210525002545975](D:\java笔记\javaweb\readme.assets\image-20210525002545975.png)

![image-20210525002557990](D:\java笔记\javaweb\readme.assets\image-20210525002557990.png)





​	5.3 踢出群聊，同样，不是管理员或者群主是不可以操作的。

​	![image-20210525002814959](D:\java笔记\javaweb\readme.assets\image-20210525002814959.png)

​	![image-20210525002831056](D:\java笔记\javaweb\readme.assets\image-20210525002831056.png)



踢出群后，比如把特朗普拜登踢出群：

![image-20210525005527012](D:\java笔记\javaweb\readme.assets\image-20210525005527012.png)

结果：

![image-20210525010545181](D:\java笔记\javaweb\readme.assets\image-20210525010545181.png)



5.4 群主直接拉好友进群

错误提示：找不到好友，该好友在群中了....

![image-20210525011055751](D:\java笔记\javaweb\readme.assets\image-20210525011055751.png)

![image-20210525011111641](D:\java笔记\javaweb\readme.assets\image-20210525011111641.png)

![image-20210525011135101](D:\java笔记\javaweb\readme.assets\image-20210525011135101.png)



如果是群主，就直接拉进群，如果是其他人，就要等待同意，下面是代码展示和群主拉人的演示

![image-20210525011516411](D:\java笔记\javaweb\readme.assets\image-20210525011516411.png)

![image-20210525011842704](D:\java笔记\javaweb\readme.assets\image-20210525011842704.png)	

这里改成已邀请进入群聊了

![image-20210525012512756](D:\java笔记\javaweb\readme.assets\image-20210525012512756.png)

添加进来了

![image-20210525012618621](D:\java笔记\javaweb\readme.assets\image-20210525012618621.png)





### 2.18 朋友圈功能

界面：

![image-20210525013444843](D:\java笔记\javaweb\readme.assets\image-20210525013444843.png)

![image-20210525013559089](D:\java笔记\javaweb\readme.assets\image-20210525013559089.png)



#### 2.18.1 查看自己朋友圈和好友朋友圈功能

点击自己朋友圈或者好友朋友圈就可以了



#### 2.18.2 添加朋友圈功能

点击添加朋友圈进入编辑界面，说明：编写朋友圈的界面参考了csdn的设计，左边可以编辑朋友圈，右边可以实时展示出来，遗憾的是没有实现草稿功能。使用了写的一个富文本编辑器，也有缺点，就是没有做一个下拉框出来。

![image-20210525014149415](D:\java笔记\javaweb\readme.assets\image-20210525014149415.png)

下面演示朋友圈输入

![image-20210525015142177](D:\java笔记\javaweb\readme.assets\image-20210525015142177.png)

发布成功后的模样

![image-20210525020345063](D:\java笔记\javaweb\readme.assets\image-20210525020345063.png)

当然，也会有敏感词检测（如八九局，敏感词是从网上找的一个敏感词表）

![image-20210525020458471](D:\java笔记\javaweb\readme.assets\image-20210525020458471.png)



#### 2.18.3 朋友圈评论功能

1. 界面说明：首先是上层的展示区，如何到了最底下的分页查询的功能，有点赞数，评论数、相关回复的数，和点赞、取消赞、发布评论、删除动态的功能。删除动态不是本人的动态是不可以操作的。评论功能也实现了评论中的评论。

   ![image-20210525094742807](D:\java笔记\javaweb\readme.assets\image-20210525094742807.png)

   

   ![image-20210525094759385](D:\java笔记\javaweb\readme.assets\image-20210525094759385.png)



2. 查看上一页、下一页、页数跳转。如果当前是在第一页是不可以点击上一页的,如果是在最后一页是不可以点击下一页的。

   ![image-20210525103825277](D:\java笔记\javaweb\readme.assets\image-20210525103825277.png)

   ![image-20210525103836532](D:\java笔记\javaweb\readme.assets\image-20210525103836532.png)

   ​	



3. 点赞功能和取消点赞功能，错误提示：已点过赞，还未点赞不可取消点赞

   ![image-20210525104009790](D:\java笔记\javaweb\readme.assets\image-20210525104009790.png)

   ![image-20210525104105321](D:\java笔记\javaweb\readme.assets\image-20210525104105321.png)



点赞后，点赞数就会改变



4. 评论发布

   ![image-20210525104447210](D:\java笔记\javaweb\readme.assets\image-20210525104447210.png)

   

   ![image-20210525104510757](D:\java笔记\javaweb\readme.assets\image-20210525104510757.png)



结果展示:会显示出发布人的名字和评论的时间评论数也会+1

![image-20210525104625611](D:\java笔记\javaweb\readme.assets\image-20210525104625611.png)

![image-20210525105143422](D:\java笔记\javaweb\readme.assets\image-20210525105143422.png)

​	

5. 评论中的评论

   ![image-20210525115905750](D:\java笔记\javaweb\readme.assets\image-20210525115905750.png)

   ![image-20210525115934045](D:\java笔记\javaweb\readme.assets\image-20210525115934045.png)



6. 删除动态，不是本人的不能删除

   ![image-20210525120216276](D:\java笔记\javaweb\readme.assets\image-20210525120216276.png)

   例如删除id为4的动态

   ![image-20210525120242424](D:\java笔记\javaweb\readme.assets\image-20210525120242424.png)

   效果

   ![image-20210525120259175](D:\java笔记\javaweb\readme.assets\image-20210525120259175.png)



### 3.1 管理员功能

#### 3.1.1 登陆

![image-20210525131226243](D:\java笔记\javaweb\readme.assets\image-20210525131226243.png)

错误提示：比普通用户多了一个不是管理员，没有权限，其他和用户登陆的一样，就不多测试了

![image-20210525131602085](D:\java笔记\javaweb\readme.assets\image-20210525131602085.png)



#### 3.1.2 封号和解封

错误提示：未封号不可解封和已封号不可再封号提示以及没有权限，说明：封号后会进入延时队列中

![image-20210525133212861](D:\java笔记\javaweb\readme.assets\image-20210525133212861.png)

![image-20210525135704956](D:\java笔记\javaweb\readme.assets\image-20210525135704956.png)

![image-20210525140528784](D:\java笔记\javaweb\readme.assets\image-20210525140528784.png)

![image-20210525141910935](D:\java笔记\javaweb\readme.assets\image-20210525141910935.png)







封号：1-999天选一个

![image-20210525133356745](D:\java笔记\javaweb\readme.assets\image-20210525133356745.png)

![image-20210525133406981](D:\java笔记\javaweb\readme.assets\image-20210525133406981.png)

效果：

![image-20210525133517235](D:\java笔记\javaweb\readme.assets\image-20210525133517235.png)

![image-20210525140127779](D:\java笔记\javaweb\readme.assets\image-20210525140127779.png)





解封：

![image-20210525133547078](D:\java笔记\javaweb\readme.assets\image-20210525133547078.png)

效果，可以登陆进来：

![image-20210525133606021](D:\java笔记\javaweb\readme.assets\image-20210525133606021.png)





#### 3.1.3 通过账号查找任一用户的信息和朋友圈

1. 查找用户

![image-20210525140700227](D:\java笔记\javaweb\readme.assets\image-20210525140700227.png)



2. 查找朋友圈

   ![image-20210525140821564](D:\java笔记\javaweb\readme.assets\image-20210525140821564.png)

   

管理员没有权利删除管理员

![image-20210525142050209](D:\java笔记\javaweb\readme.assets\image-20210525142050209.png)



对于普通用户有权限

![image-20210525142130819](D:\java笔记\javaweb\readme.assets\image-20210525142130819.png)

![image-20210525142137522](D:\java笔记\javaweb\readme.assets\image-20210525142137522.png)

![image-20210525142146548](D:\java笔记\javaweb\readme.assets\image-20210525142146548.png)

在数据库中也删除了，22号id去除了

![image-20210525142229463](D:\java笔记\javaweb\readme.assets\image-20210525142229463.png)



### 4.1 超管功能

说明：超管只有一个，系统内定的，不可更改的，超管可以选择封为系统管理员或者取消系统管理员的功能，账号2429890953，密码13600371356Aa@

#### 4.1.1 登陆

错误提示，其他的和用户登陆的一样，账号密码错误检测，验证码错误检测

![image-20210525142801771](D:\java笔记\javaweb\readme.assets\image-20210525142801771.png)

![image-20210525143218965](D:\java笔记\javaweb\readme.assets\image-20210525143218965.png)





#### 4.1.2 查找用户并且封为管理员或取消管理员

1. 错误提示，找不到用户，不可对自己操作，对方已经是管理员，不可以设置为管理员；对方本来不是管理员，不可以解除权限，不可对游客进行操作

   ![image-20210525150544925](D:\java笔记\javaweb\readme.assets\image-20210525150544925.png)

   ![image-20210525143505579](D:\java笔记\javaweb\readme.assets\image-20210525143505579.png)

   ![image-20210525143517908](D:\java笔记\javaweb\readme.assets\image-20210525143517908.png)

   ![image-20210525143743436](D:\java笔记\javaweb\readme.assets\image-20210525143743436.png)

   ![image-20210525145442398](D:\java笔记\javaweb\readme.assets\image-20210525145442398.png)



2. 解除管理员权限

   ![image-20210525145811251](D:\java笔记\javaweb\readme.assets\image-20210525145811251.png)

   ![image-20210525145847303](D:\java笔记\javaweb\readme.assets\image-20210525145847303.png)





3. 设置管理员

   ![image-20210525145939397](D:\java笔记\javaweb\readme.assets\image-20210525145939397.png)

   ![image-20210525150001307](D:\java笔记\javaweb\readme.assets\image-20210525150001307.png)





4. 根据账号查找

   错误提示：没有找得到用户

   ![image-20210525150029840](D:\java笔记\javaweb\readme.assets\image-20210525150029840.png)

   找到了，就可以进行操作了

   ![image-20210525150051532](D:\java笔记\javaweb\readme.assets\image-20210525150051532.png)





### 5. 游客功能

#### 5.1登陆

说明：界面错误提示和用户登陆不同的是多了一个你不是游客提示，其他一样

![image-20210525161053928](D:\java笔记\javaweb\readme.assets\image-20210525161053928.png)



#### 5.2 游客界面

说明：左边是好友，下面是一些好友操作，游客只能进行添加删除拉黑好友和进入聊天室，不能进行其他操作

![image-20210525161130506](D:\java笔记\javaweb\readme.assets\image-20210525161130506.png)

![image-20210525161338189](D:\java笔记\javaweb\readme.assets\image-20210525161338189.png)





1. 拉黑和取消拉黑，拉黑后进入聊天室就看不见好友了

   ![image-20210525161418788](D:\java笔记\javaweb\readme.assets\image-20210525161418788.png)

   ![image-20210525162244460](D:\java笔记\javaweb\readme.assets\image-20210525162244460.png)



取消拉黑之后又可以聊天了

​		![image-20210525162648521](D:\java笔记\javaweb\readme.assets\image-20210525162648521.png)

![image-20210525162700173](D:\java笔记\javaweb\readme.assets\image-20210525162700173.png)





2. 查找添加好友

   ![image-20210525162951404](D:\java笔记\javaweb\readme.assets\image-20210525162951404.png)

   ![image-20210525162959298](D:\java笔记\javaweb\readme.assets\image-20210525162959298.png)

   特朗普拜登界面：

   ![image-20210525163053589](D:\java笔记\javaweb\readme.assets\image-20210525163053589.png)

   ![image-20210525163444890](D:\java笔记\javaweb\readme.assets\image-20210525163444890.png)



3. 删除好友

   ![image-20210525163828870](D:\java笔记\javaweb\readme.assets\image-20210525163828870.png)

   ![image-20210525164301358](D:\java笔记\javaweb\readme.assets\image-20210525164301358.png)





4. 进入好友聊天

   ![image-20210525164322389](D:\java笔记\javaweb\readme.assets\image-20210525164322389.png)

点击进入就看可以了，和好友聊天界面是一样的。







