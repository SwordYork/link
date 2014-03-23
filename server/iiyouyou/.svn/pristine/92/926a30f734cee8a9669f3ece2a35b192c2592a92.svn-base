<?php
//消息推送Demo
//增加了IOS的离线消息推送,IOS不支持IGtNotyPopLoadTemplate模板
//更新时间为2014年01月13日 VERSION:3.0.0.1
//IOS用户，增加PushInfo的长度判断，超过256字节的长度则禁止发送，android用户请注释 setPushInfo字段
//一个中文汉字为3个字节，一个英文与一个数字为一个字节
//增加用户状态查询接口
//增加任务停止功能

header("Content-Type: text/html; charset=utf-8");

require_once "../lib/connectDb.php";
require_once('../igetui/' . 'IGt.Push.php');

define('APPKEY','GRw8XjJyi07lsPSwigac59');
define('APPID','tuiS0dcMLk80HeLrd5cfc7');
define('MASTERSECRET','NYJbzZKVg66bwZ4unbEiu5');
define('HOST','http://sdk.open.api.igexin.com/apiex.htm');


//$userid = $_SESSION['userid'];

$db = connectDb();

$query =  sprintf("SELECT * FROM link_clientid");
//$query =  sprintf("SELECT * FROM link_clientid");
$result = $db->query($query);
if (!$result){
	   $response = array('send_request'=>'sqlerror');
	   echo "eeeeeeee";
}
else{
	    $targetList = array();
	    while($row = $result->fetch_assoc()){
	      $target1 = new IGtTarget();
	      $target1->set_appId(APPID);
	      $target1->set_clientId($row["client_id"]);
	      array_push($targetList,$target1);
	    }
	    pushMessageToList($targetList);
	    echo "ssssssss";
	    echo $targetList;
}




//getUserStatus();

//stoptask();

//pushMessageTransmission();

//pushMessageToSingle();



//pushMessageToApp();

//用户状态查询
function getUserStatus() {
    $igt = new IGeTui(HOST,APPKEY,MASTERSECRET);
    $rep = $igt->getClientIdStatus(APPID,CID);
    var_dump($rep);
    echo ("<br><br>");
}

function stoptask(){
	$igt = new IGeTui(HOST,APPKEY,MASTERSECRET);
	$igt->stop("OSA-0113_O2J1RZmkBg6ektb7genfS6");
}

//单推消息透传案例
function pushMessageTransmission($t_content){
	$igt = new IGeTui(HOST,APPKEY,MASTERSECRET);
	
	$template =  new IGtTransmissionTemplate(); 

	$template ->set_appId(APPID);//应用appid
	$template ->set_appkey(APPKEY);//应用appkey
	//透传
	$template ->set_transmissionType("2");//透传类型 1,应用立即启动，2，客户端处理启动
	$template ->set_transmissionContent($t_content);//透传内容
	// iOS推送需要设置的pushInfo字段
	//$template ->set_pushInfo($actionLocKey,$badge,$message,$sound,$payload,$locKey,$locArgs,$launchImage);
	$template ->set_pushInfo("", 0, "", "", "", "", "", "");
	//个推信息体
	$message = new IGtSingleMessage();

	$message->set_isOffline(true);//是否离线
	$message->set_offlineExpireTime(1000*3600*12);//离线时间
	$message->set_data($template);//设置推送消息类型

	//接收方
	$target = new IGtTarget();
	$target->set_appId(APPID);
	$target->set_clientId(CID);

	$rep = $igt->pushMessageToSingle($message,$target);

	var_dump($rep);
    echo ("<br><br>");
}


//单推消息通知弹框下载动作案例，IOS暂不支持
function pushMessageToSingle(){
	$igt = new IGeTui(HOST,APPKEY,MASTERSECRET);
	
	$template =  new IGtNotyPopLoadTemplate(); 

	$template ->set_appId(APPID);//应用appid
	$template ->set_appkey(APPKEY);//应用appkey
	//通知栏
	$template ->set_notyTitle("个推");//通知栏标题
	$template ->set_notyContent("个推最新版点击下载");//通知栏内容
	$template ->set_notyIcon("icon.png");//通知栏logo
	$template ->set_logoURL("http://www.photophoto.cn/m23/086/010/0860100017.jpg");//通知栏网络图片展示
	$template ->set_isBelled(true);//是否响铃
	$template ->set_isVibrationed(true);//是否震动
	$template ->set_isCleared(true);//通知栏是否可清除
	//弹框
	$template ->set_popTitle("弹框标题");//弹框标题
	$template ->set_popContent("弹框内容");//弹框内容
	$template ->set_popImage("");//弹框图片
	$template ->set_popButton1("下载");//左键
	$template ->set_popButton2("取消");//右键
	//下载
	$template ->set_loadIcon("http://www.photophoto.cn/m23/086/010/0860100017.jpg");//弹框图片
	$template ->set_loadTitle("地震速报下载");
	$template ->set_loadUrl("http://dizhensubao.igexin.com/dl/com.ceic.apk");
	$template ->set_isAutoInstall(false);
	$template ->set_isActived(true);

	//个推信息体
	$message = new IGtSingleMessage();

	$message->set_isOffline(true);//是否离线
	$message->set_offlineExpireTime(3600*12*1000);//离线时间
	$message->set_data($template);//设置推送消息类型

	//接收方
	$target = new IGtTarget();
	$target->set_appId(APPID);
	$target->set_clientId(CID);

	$rep = $igt->pushMessageToSingle($message,$target);

	var_dump($rep);
    echo ("<br><br>");
}


//多推消息通知打开网页案例
function pushMessageToList($targetList){
	$igt = new IGeTui(HOST,APPKEY,MASTERSECRET);

	$template =  new IGtNotyPopLoadTemplate(); 

	$template ->set_appId(APPID);//应用appid
	$template ->set_appkey(APPKEY);//应用appkey
	//通知栏
	$template ->set_notyTitle("link");//通知栏标题
	$template ->set_notyContent("更新link");//通知栏内容
	$template ->set_notyIcon("icon.png");//通知栏logo
	$template ->set_logoURL("");//通知栏网络图片展示
	$template ->set_isBelled(true);//是否响铃
	$template ->set_isVibrationed(true);//是否震动
	$template ->set_isCleared(true);//通知栏是否可清除
	//弹框
	$template ->set_popTitle("注意");//弹框标题
	$template ->set_popContent("如果安装错误，先删除原应用");//弹框内容
	$template ->set_popImage("");//弹框图片
	$template ->set_popButton1("下载");//左键
	$template ->set_popButton2("取消");//右键
	//下载
	$template ->set_loadIcon("");//弹框图片
	$template ->set_loadTitle("link更新");
	$template ->set_loadUrl("http://iiyouyou.sinaapp.com/link.apk");
	$template ->set_isAutoInstall(false);
	$template ->set_isActived(true);

	// iOS推送需要设置的pushInfo字段
        //$template ->set_pushInfo($actionLocKey,$badge,$message,$sound,$payload,$locKey,$locArgs,$launchImage);
	
	//个推信息体
	$message = new IGtSingleMessage();

	$message->set_isOffline(true);//是否离线
	$message->set_offlineExpireTime(3600*12*1000);//离线时间
	$message->set_data($template);//设置推送消息类型
	
	$contentId = $igt->getContentId($message);

	
	
	//接收方1	
	
	$rep = $igt->pushMessageToList($contentId, $targetList);

	var_dump($rep);
    echo ("<br><br>");

}

//群推通知透传案例
function pushMessageToApp(){
	$igt = new IGeTui(HOST,APPKEY,MASTERSECRET);
	
	//消息类型 : 状态栏通知 点击通知启动应用
	$template =  new IGtNotificationTemplate(); 

	$template->set_appId(APPID);//应用appid
	$template->set_appkey(APPKEY);//应用appkey
	$template->set_transmissionType(2);//透传消息类型
	$template->set_transmissionContent("测试离线");//透传内容
	$template->set_title("个推");//通知栏标题
	$template->set_text("个推最新版点击下载");//通知栏内容
	$template->set_logo("icon.png");//通知栏logo
	$template ->set_logoURL("http://www.photophoto.cn/m23/086/010/0860100017.jpg");//通知栏网络图片展示
	$template->set_isRing(true);//是否响铃
	$template->set_isVibrate(true);//是否震动
	$template->set_isClearable(true);//通知栏是否可清除
	// iOS推送需要设置的pushInfo字段
	//$template ->set_pushInfo($actionLocKey,$badge,$message,$sound,$payload,$locKey,$locArgs,$launchImage);
	
	//个推信息体
	//基于应用消息体
	$message = new IGtAppMessage();

	$message->set_isOffline(true);
	$message->set_offlineExpireTime(3600*12*1000);//离线时间单位为毫秒，例，两个小时离线为3600*1000*2
	$message->set_data($template);

 
	$message->set_appIdList(array(APPID));
	$message->set_phoneTypeList(array('ANDROID'));
//	$message->set_provinceList(array('浙江','北京','河南'));
//	$message->set_tagList(array('开心'));

	$rep = $igt->pushMessageToApp($message);

	var_dump($rep);
    echo ("<br><br>");

}


 
?>
 
