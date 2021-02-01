package cn.enilu.flash.bean.vo.front;

public class Rets {

    public static final Integer SUCCESS = 20000;
    public static final Integer FAILURE = 9999;
    public static  final Integer TOKEN_EXPIRE=50014;
    public  static final Integer  Result_Code_LoginOut=100001;//请登录
	public  static final Integer  Result_Code_No_Authority=100002;
	public  static final Integer  Result_Code_No_OrgCode=100003;//orgcode不存在
	public  static final Integer  Result_Code_Auth_Times=100004;//授权已过期

    public static Ret success(Object data) {
        return new Ret(Rets.SUCCESS, "成功", data);
    }

    public static Ret failure(String msg) {
        return new Ret(Rets.FAILURE, msg, null);
    }
    public static Ret error(Integer code,String msg) {
    	return new Ret(code, msg, null);
    }

    public static Ret success() {
        return new Ret(Rets.SUCCESS, "成功", null);
    }
    public static  Ret expire(){
        return new Ret(Rets.TOKEN_EXPIRE,"token 过期",null);
    }
}
