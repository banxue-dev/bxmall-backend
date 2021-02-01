package cn.enilu.flash.api.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import cn.enilu.flash.bean.vo.front.Ret;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.security.JwtUtil;
import cn.enilu.flash.utils.GlobayConst;
import cn.enilu.flash.utils.StringUtil;


/**
 * 处理跨域
 * @author tWX932595
 *
 */
@WebFilter(urlPatterns = "/*")
public class SourceFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(SourceFilter.class);
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
        //初始化filter时手动注入bean对象
		
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		//处理options请求
        if (req.getMethod().toUpperCase().equals("OPTIONS")) {
        	chain.doFilter(request,response);
            return;
        }
		logger.debug("开始过滤");
        /**
         * 1、先验证要忽略的其中api接口必须要orgcode
         * 2、其他接口里面，系统接口需要验证是否有这些系统设置的数据权限，防止侵入
         * 3、剩余的就只验证是否登录
         */
		String url = req.getRequestURL().toString();
		// 获取token
		String auth = req.getHeader(GlobayConst.TokenName);
		//获取orgcode
		String orgCode=req.getParameter(GlobayConst.ConstParamOrgCodeKey);
		String iron=getFilterUrl(url,  "/api/","/login","logout","/images/","/getLeftMenu");//要忽略的
		if(iron!=null) {
			if(iron.contentEquals( "/api/")) {
				//api接口需要orgcode
				if(StringUtil.isEmpty(orgCode)) {
					returnNotOrgCode(res);
					return;
				}
			}
			chain.doFilter(request,response);
            return ;
		}
		
		if(StringUtil.isNullStrings(auth,orgCode)) {
			logger.info("请求：{},没有token和orgCode!", url);
		}else {
			String username = JwtUtil.getUsername(auth);
			if(StringUtil.isEmpty(username)) {
				
			} else {
				logger.info("请求：{},token已失效!", url);
			}
		}
		res.addHeader("Access-Control-Allow-Origin","*");
		res.addHeader("Access-Control-Allow-Credentials","true");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With,X-App-Id, X-Token");
        res.addHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
        res.addHeader("Access-Control-Max-Age", "3600");
        res.addHeader("Content-Type", "application/json;charset=UTF-8");
		returnNotLogin(res);
		return;

	}
	private static void returnNotLogin(HttpServletResponse res) {
		Ret body=Rets.error(Rets.Result_Code_LoginOut,"Pleace Login");
		byte[] bytes = JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
		try {
			res.getWriter().write(new String(bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("打错误:"+e);
		}
	}
	private static void returnNotOrgCode(HttpServletResponse res) {
		Ret body=Rets.error(Rets.Result_Code_No_OrgCode,"Api interface need orgcode");
		byte[] bytes = JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
		try {
			res.getWriter().write(new String(bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("打错误:"+e);
		}
	}
	private static void returnNoAuthority(HttpServletResponse res) {
		Ret body=Rets.error(Rets.Result_Code_No_Authority,"No Authority");
		byte[] bytes = JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
		try {
			res.getWriter().write(new String(bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("打错误:"+e);
		}
	}
	
	/**
	 * 给定的string[]只要有一个就直接过滤
	 * @param url
	 * @param str
	 * @return
	 */
	private static boolean filterUrl(String url,String ... strs) {
		for(String str:strs) {
			if( url.lastIndexOf(str)>0) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 给定的string[]获取是那一个
	 * @param url
	 * @param str
	 * @return
	 */
	private static String getFilterUrl(String url,String ... strs) {
		for(String str:strs) {
			if( url.lastIndexOf(str)>0) {
				return str;
			}
		}
		return null;
	}

}
