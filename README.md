# API 规范

服务器地址：wss://192.168.22.145:8443/groupcall
数据库服务器地址:jdbc:mysql://192.168.22.145:3306/WeaksCall?useSSL=false&useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull

客户端发送数据规范（json）：

| 名称 | 字段名称 | 数据类型 | 默认值 | 描述
| --- | --- | --- | --- | ---
| 请求加入房间 | type | string | 'join' | 请求名
|		    | room | number | 0 | 房间号
|		    | userId | number | —— | 用户id
|		    | (pw) | string | —— | 房间密码
| | | | | 
| room 内发送消息 | type | string | 'sendMessage' | 请求名
|              | message | string | —— | 具体信息 
| | | | | 
| 上传文件完成 | type | string | 'uploadSuccess' | 请求名
|            | fileName | string | —— | 文件名
|            | fileUrl | string | —— | 文件服务器提供的路径
| | | | | 
| 发送打洞方式 | type | string | 'onIceCandidate' | 请求名
|		    | userId | number | —— | 用户id
|		    | candidate | iceCandidate| —— | 打洞信息
| | | | | 
| 从远端接收视频流请求 | type | string | 'receiveVideoFrom' | 请求名
|				 | userId | number | —— | 待接收用户id
|				 | sdpOffer | sdpOffer | —— | sdp视频请求
          
客户端接收数据规范（json）：

| 名称 | 字段名称 | 数据类型 | 默认值 | 描述
| --- | --- | --- | --- | ---
| 用户不存在 | type | string | 'userNotFound' | 请求名
| | | | | | 
| 没有权限进入此房间| type | string | 'permisionDeny' | 请求名
| | | | | | 
| 存在的参与者 | type | string | 'existingParticipants' | 请求名
|           | participants | Array<{id, name, prefix, postfix}> | [] | 房间内存在的参与者，不包括自己
| | | | | | 
| 新参与者加入时 | type | string | 'newParticipantArrived' | 请求名
|              | participant | {id, name, prefix, postfix } | —— | 新加入的参与者
| | | | | | 
| 收到参与者视频应答时 | type | string | 'receiveVideoAnswer' | 请求名
|                 | userId | number | —— | 视频对应的用户id
|                 | sdpAnswer | sdpAnswer | —— | 视频数据
| | | | | | 
| 参与者离开时 | type | string | 'participantLeft' | 请求名
|            | userId | number | —— | 要离开的参与者id
| | | | | | 
| 当链接需要打洞时 | type | string | 'iceCandidate' | 请求名
|                | userId | number | —— | 待打洞的用户id
|                | candicate | candidate | —— | 打洞信息
| | | | | | 
| 当有人在 room 内发送文字消息时 | type | string | 'reciveTextMessage' | 请求名
|                            | userId | number | —— | 发送消息的用户id
|                            | message | string | '' | 消息内容
| | | | | | 
| 当有人在 room 上传了文件时 | type | string | 'fileUploaded' | 请求名
|                         | userId | number | —— | 上传文件的用户id
|                         | fileName | string | '' | 文件名
|                         | fileType | string | —— | 文件类型
|                         | fileUrl | string | —— | 文件服务器提供的路径
| | | | | |
| 服务器无法识别消息类型时 | type | string | 'unrecognizedMessage' | 请求名 