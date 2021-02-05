package cn.enilu.flash.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 组织代码生成器
 * @author tWX932595
 *
 */
public class OrgCodeGreater {

	private static Logger logger=LoggerFactory.getLogger(OrgCodeGreater.class);
	private static String[] code= {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z" };
	private static String[] passc= {"1","5","2","0","9","7","6","3","4","8","G","H","I","o","p","J","D","E","F","s","t","u","v","w","x","y","z","X","A","Y","Z","a","b","c","d","e","f","g","h","i","j","k","K","L","M","N","O","P","Q","R","S","T","U","V","W","B","C","l","m","n","q","r" };
	private static List<String> passArr=Arrays.asList(passc);
	public static String createCode() {
		String dates=TimeUtils.getCurrentTime("yyyyMMddHHmmss{0}{1}{2}{3}{4}");
		int len=5;
		for(int i=0;i<len;i++) {
			String th=getRomEn();
			dates=dates.replace("{"+i+"}", th);
		}
		return dates;
	}
	private static Random r = new Random();
	public static String getRomEn() {
		String str="";
		for(int i=0;i<5;i++) {
			int res=r.nextInt(51);
			System.out.println(res);
			str+=code[res];
		}
		return str;
	}
	/**
	 * 加密
	 * @param source
	 * @return
	 */
	public static String encode(String source) {
		if(StringUtil.isEmpty(source)) {
			return null;
		}
		String res="";
		char[]  c = source.toCharArray();

		for(int i=0;i < c.length;i++) {
			String t=String.valueOf(c[i]);
			res+=passArr.indexOf(t);
		}
		return res;
	}
	/**
	 * 解密
	 * @param source
	 * @return
	 */
	public static String decode(String source) {
		
		String res="";
		if(StringUtil.isEmpty(source)) {
			return null;
		}
		if(source.length()<64) {
			return source;
		}
		try {
			char[]  c = source.toCharArray();
			String tempr="";
			int i=0;
			while(i<c.length) {

				tempr=String.valueOf(c[i]);
				i++;
				if(i-1>13 ) {
					tempr+=String.valueOf(c[i]);
					i++;
				}
				int t=Integer.parseInt(tempr);
				res+=passc[t];
			}
			if(res.contentEquals(GlobayConst.AdminOrgCode)) {
				logger.debug("当前是管理员账户"+source);
				return null;//是admin就不查询了
			}
		}catch(Exception e) {
			logger.error("解码错误"+e);
			res=source;
		}
		return res;
	}
	public static void main(String[] args) {
//		049930383738313628345839592556165049312543211225571052274535402552
//		String sr=createCode();
//		System.out.println(sr);
		String ec=encode("20201012085851dbHAcqckJunqhmPuVJiTDUidE");
		System.out.println(ec);
		String dc=decode("2727373274040378720029776377800120146379198520170174100612747805");
		System.out.println(dc.contentEquals("20201012085851dbHAcqckJunqhmPuVJiTDUidE"));
		System.out.println(dc);
		BigDecimal  bd=new BigDecimal("-0.1");
		System.out.println(bd.toString());
	}
	
}

