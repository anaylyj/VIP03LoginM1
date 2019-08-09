/**
 * 
 */

function loginAjax(){
	$.ajax({
		//指定调用的接口的url资源地址,url/type等不同参数之间用,分割
		url : "./Login",
		//指定HTTP方法
		type : "post",
		//返回数据提交给ajax处理的类型,text代表纯文本格式的字符串
		dataType:"json",
		//ajax向接口提交的数据,读取指定form元素当中的内容，通过serialize方法进行初始化。
		data : $("#loginForm").serialize(),
		//ajax请求完成拿到返回之后进行的处理操作：一种是成功的时候，一种是失败的时候
		//ajax提交请求之后得到的返回结果，就是result，不需要自己进行定义
		success : function(result){
//			alert(result);
			document.getElementById("msgDiv").innerText=result["msg"];
			if(result["msg"]=="恭喜您，登录成功!")
			{
			window.location.href="user.html";
			}
		},
		error : function(result){
			document.getElementById("msgDiv").innerText="接口请求错误，请检查";
		}	
		
	})
	
	
}

/**
 * getUser函数，通过user.html中body的onload事件响应异步提交
 * 在访问user.html时就调用Userinfo接口获取用户信息并且写到对应的元素中。
 */
function getUser() {
	// 定义一个存放url的变量，指定请求的接口的地址
	var AjaxURL = "./UserInfo";
	$.ajax({
				// 方法用post
				type : "post",
				// 返回和请求的参数类型传递方式。
				dataType : "json",
				// 请求的接口地址
				url : AjaxURL,
				// 接口执行的返回，当接口调用成功时，执行success中的方法
				success : function(result) {
					// 将返回结果解析出来的对应内容填写到对应的元素中
					document.getElementById("userid").innerHTML = result["id"];
					document.getElementById("nickname").innerHTML = result["nickname"];
					document.getElementById("describe").innerHTML = result["describe"];
				},
				// 接口调用出错时，执行该方法
				error : function() {
					alert("调用UserInfo接口出错，请检查。");
				}
			});
}
/**
 * logout()方法在user.html中的注销按钮通过onclick事件响应 调用Logout接口，完成注销的操作。
 */
function logout() {
	// 定义一个存放url的变量，指定请求的接口的地址
	var AjaxURL = "./Logout";
	$.ajax({
		// 方法用post
		type : "post",
		// 返回和请求的参数类型传递方式。
		dataType : "json",
		// 请求的接口地址
		url : AjaxURL,
		// 接口执行的返回，当接口调用成功时，执行success中的方法
		success : function(result) {
			alert(result["msg"]);
			window.location.href = "index.html";
		},
		// 接口调用出错时，执行该方法
		error : function() {
			alert("调用Logout接口出错，请检查。");
		}
	});
}