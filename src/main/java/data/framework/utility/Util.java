package data.framework.utility;

/**
 * Title:        基本工具方法类
 * Description:  常用的方法
 *
 */

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.lang.text.StrTokenizer;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import data.framework.support.ConfigContext;

public class Util {
    private static Random random = new Random();


    /**
     * 取统一授权应用是否开启
     * @return
     */
    public static boolean getBuaOpen() {
        return "true".equalsIgnoreCase( ConfigContext.getStringSection("framework.bua.open"));
    }
    /**
     * 取统一授权应用的地址
     * @return
     */
    public static String getBuaLocation() {
        return ConfigContext.getStringSection("framework.bua.loc");
    }
    public static String returnNullValue(String s) {
    	if (s == null || "null".equals(s)) {
    		return "";
    	}
    	return s;
    }
    /**
     * 取spring配置里的bean
     * @param beanName
     * @param request
     * @return
     */
    public static Object getBean(String beanName,HttpServletRequest request) {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext( ((WebApplicationContext) request).getServletContext());
        return wac.getBean(beanName);
    }

    /**
     * 取spring配置里的bean
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        WebApplicationContext wac= ContextLoader.getCurrentWebApplicationContext();
        return wac.getBean(beanName);
    }

    public Util() {
    }
    /**
     * 字符串转换成布尔型，默认返回false
     * @param strVal
     * @return
     */
    public static boolean str2bool(String strVal){
    	boolean boolVal = false;
    	try{
    		if(!"".equals(strVal)) {
    			boolVal = Boolean.valueOf(strVal);
    		}
    	}catch(Exception e){
    		boolVal = false;
    	}
    	return boolVal;
    }
    
    /*public static String[] TokenizerStringNew(String str, String dim) {
        return new StrTokenizer(str, dim).getTokenArray();
    } */

    public static ArrayList TokenizerString(String str, String dim) {
        return TokenizerString(str, dim, false);
    }

    /**
     * 为系统浮点数数据补0的方法
     * qc75759
     * @param aNumber 原数组值
     * @param precision 需要保留的精度
     * @return
     */
    public static String toDecimalDigits(String aNumber,int precision){
        String returnVal = aNumber;
        try{
        if(null == aNumber || aNumber.equals("")){
            return  "";
        }       
        int valInt = 0;
        if(aNumber.indexOf(".")>-1){
            String[] val = aNumber.split("\\.");
            valInt = val[val.length-1].length();
        } 
        if(valInt != precision){
            if(valInt == 0){
                if(precision == 1){
                    returnVal += ".0";
                }else if(precision == 2){
                    returnVal += ".00";
                }else if(precision == 3){
                    returnVal += ".000";
                }else if(precision == 4){
                    returnVal += ".0000";
                }
            }else{
                int lengInt = precision-valInt;
                //判断添加小数位0的个数
                if(lengInt == 1 ){
                    returnVal += "0";
                }else if(lengInt == 2){
                    returnVal += "00";
                }else if(lengInt == 3){
                    returnVal += "000";
                }else if(lengInt < 0){
                    /*if(precision == 1){
                        returnVal += ".0";
                    }else if(precision == 2){
                        returnVal += ".00";
                    }else if(precision == 3){
                        returnVal += ".000";
                    }else if(precision == 4){
                        returnVal += ".0000";
                    }  */ 
                	//liuzy 上述做法会将123.45678转换成123.45678.00,算法改为四舍五入
                	Double val= Double.parseDouble(returnVal);
                	BigDecimal bd=new BigDecimal(val);
            		bd=bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
            		returnVal = String.valueOf(bd.doubleValue());
                }
            }           
        }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return  returnVal;  
    }
    
    public static ArrayList TokenizerString(String str, String dim, boolean returndim) {
        str = null2String(str);
        dim = null2String(dim);
        ArrayList strlist = new ArrayList();
        StringTokenizer strtoken = new StringTokenizer(str, dim, returndim);
        while (strtoken.hasMoreTokens()) {
            strlist.add(strtoken.nextToken());
        }
        return strlist;
    }
    public static char getSeparator_temp() {
        return 3;
    }
    public static String[] TokenizerString2(String str, String dim) {
        return TokenizerString2(str, dim, false);
    }

    public static String[] TokenizerString2(String str, String dim, boolean returndim) {
        ArrayList strlist = TokenizerString(str, dim, returndim);
        int strcount = strlist.size();
        String[] strarray = new String[strcount];
        for (int i = 0; i < strcount; i++) {
            strarray[i] = (String) strlist.get(i);
        }
        return strarray;
    }

    public static String add0(int v, int l) {
        long lv = (long) Math.pow(10, l);
        return String.valueOf(lv + v).substring(1);
    }

    public static String getCookie(HttpServletRequest req, String key) {
    	try {
	        Cookie[] cookies = req.getCookies();
			if(cookies != null && cookies.length >0){
			  for (int i = 0; i < cookies.length; i++) {
				 if (cookies[i].getName().equals(key)) {
	                return cookies[i].getValue();
				 }
			  }
			}
    	}
    	catch(Exception e) {
    	}
        return null;
    }

    public static void setCookie(HttpServletResponse res, String key, String value, int age, String domain) {
				
				try {
					value = java.net.URLEncoder.encode(value,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
        Cookie newCookie = new Cookie(key, value);
        newCookie.setMaxAge(age);
        newCookie.setDomain(domain);
        newCookie.setPath("/");

        res.addCookie(newCookie);

    }

    public static void setCookie(HttpServletResponse res, String key, String value, int age) {
				
				try {
					value = java.net.URLEncoder.encode(value,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
        Cookie newCookie = new Cookie(key, value);
        newCookie.setMaxAge(age);
        newCookie.setPath("/");
        res.addCookie(newCookie);

    }

    public static void setCookie(HttpServletResponse res, String key, String value) {
        setCookie(res, key, value, -1);
    }

    public static String null2String(String s) {
        return s == null ? "" : s;

    }
    public static String null2String(String s,String def) {
        return s == null ?(def==null?"":def) : s;

    }
    
    public static String null2String(Object s) {
        return s == null ? "" : s.toString();

    }
    //s为null或""时候返回0
    public static String null2o(String s) {
        return (s == null||s.equals("")) ? "0" : s;
    }
    
    /**
     * 转换页面中的特殊字符
     * @param s
     * @return
     */
    public static String convertInput2DB(String s)
    {
        int i = 0;
        char ch;
        StringBuffer buf = new StringBuffer();
        
        s = null2String(s);
        char c[] = s.toCharArray();

        while (i < c.length)
        {
            ch = c[i++];

            if (ch == '\'')
            {
                buf.append("\'\'");
            }
            /*else if (ch == '<')
            {
                buf.append("&lt;");
            }              
            else if (ch == '>')
            {
                buf.append("&gt;");
            }
			*/
            else if (ch == '"')
            {
                buf.append("&quot;");
            }
            else if (ch == '\n')
            {
                buf.append("<br>");
            }
            else if (ch == '\r')
            {
                buf.append("");
            }
            else
            {
                buf.append(ch);
            }
        }
       
        return buf.toString();
    }

	public static String convertInput2DB2(String s)
    {
        int i = 0;
        char ch;
        StringBuffer buf = new StringBuffer();
        
        s = null2String(s);
        char c[] = s.toCharArray();

        while (i < c.length)
        {
            ch = c[i++];

            if (ch == '<')
            {
                buf.append("&lt;");
            }
            else if (ch == '>')
            {
                buf.append("&gt;");
            }
            else if (ch == '"')
            {
                buf.append("&quot;");
            }
            else if (ch == '\n')
            {
                buf.append("<br>");
            }
            else if (ch == ' '){
            	buf.append("&nbsp;");
            }
            else if (ch == '\r')
            {
                buf.append("");
            }
            else
            {
                buf.append(ch);
            }
        }
       
        return buf.toString();
    }


	public static String convertInput2DB3(String s)
    {
        int i = 0;
        char ch;
        StringBuffer buf = new StringBuffer();
        
        s = null2String(s);
        char c[] = s.toCharArray();

        while (i < c.length)
        {
            ch = c[i++];

            if (ch == ' ')
            {
                buf.append("&nbsp;");
            }
            else if (ch == '"')
            {
                buf.append("&quot;");
            }
            else if (ch == '\n')
            {
                buf.append("<br>");
            }
            else if (ch == '\r')
            {
                buf.append("");
            }
            else
            {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
    
	public static String convertInput2DB4(String s)
  {
      int i = 0;
      char ch;
      StringBuffer buf = new StringBuffer();
      
      s = null2String(s);
      char c[] = s.toCharArray();

      while (i < c.length)
      {
          ch = c[i++];

          if (ch == '<')
          {
              buf.append("&lt;");
          }
          else if (ch == '>')
          {
              buf.append("&gt;");
          }
          else if (ch == '"')
          {
              buf.append("&quot;");
          }
          else if (ch == '\n')
          {
              buf.append("");
          }
          else if (ch == ' '){
          	//buf.append("&nbsp;");
          }
          else if (ch == '\r')
          {
              buf.append("");
          }
          else
          {
              buf.append(ch);
          }
      }
     
      return buf.toString();
  }    
    /**
     * 转换特殊字符至显示
     * @param s
     * @return
     */
    public static String convertDB2Input(String s)
    {
        return StringReplace(s, "<br>", "\n");
    }
    
    public static String fromScreen(String s, int languageid) {
        if (s == null)
            s = null2String(s);
        s = fromBaseEncoding(s, languageid);
        s = toHtml(s);
        return s;
    }
    
    public static String fromScreenVoting(String s, int languageid) {
        if (s == null)
            s = null2String(s);
        s = fromBaseEncoding(s, languageid);
        s = toHtmlVoting(s);
        return s;
    }

    public static String fromScreen2(String s, int languageid) {
        if (s == null)
            s = null2String(s);
        s = fromBaseEncoding2(s, languageid);
        s = toHtml(s);
        return s;
    }

    //this method added by xwj
    public static String fromScreen3(String s, int languageid) {
        if (s == null)
            s = null2String(s);
        s = fromBaseEncoding(s, languageid);
        s = toHtml5(s);
        return s;
    }

    public static String fromScreen4(String s, int languageid) {
        if (s == null)
            s = null2String(s);
        s = fromBaseEncoding2(s, languageid);
        //TD12523
        //s = toExcel(s);
        s = toExcelData(s);
        s = replaceHtml(s);
        return s;
    }


    /**
     * EXCEL导出数据去除HTML元素（TD12523）
     */
    public static String toExcelData(String s) {
 	    s=StringReplace(s,"&lt;", "<");  
  	    s=StringReplace(s,"&gt;", ">");
    	s=StringReplace(s,"<br>",""+'\n');
		s=StringReplace(s,"&quot;", '"'+"");
    	s=StringReplace(s,"&nbsp;"," ");
    	s=StringReplace(s,"&amp;","&");
    	s=StringReplace(s,"&ldquo;","“");
    	s=StringReplace(s,"&rdquo;","”");
    	s=StringReplace(s,"&lsquo;","‘");
    	s=StringReplace(s,"&rsquo;","’");
    	s=StringReplace(s,"&hellip;","…");
    	s=StringReplace(s,"&mdash;","—");
    	s=StringReplace(s,"&apos;","'");
    	return s;
    }
    
    public static String replaceHtml(String s) {
    	// 去掉所有html元素
        s = s.replaceAll("\\&[a-zA-Z]{1,10};", "");
        s = s.replaceAll("initFlashVideo();", "");
		s = s.replaceAll("%nbsp","");
        return s;
    }
    
    public static String toScreen(String s, int languageid) {
        return toScreen(s, languageid, "1");
    }

    public static String toScreen(String s, int languageid, String fromdb) {
        if (s == null)
            s = null2String(s);
        s = toBaseEncoding(s, languageid, fromdb);
        s = fromHtml(s);
        return s;
    }
    

    public static String toScreenToEdit(String s, int languageid) {
        if (s == null)
            s = null2String(s).trim();
        s = toBaseEncoding(s, languageid, "1");
        s = fromHtmlToEdit(s);
        return s;
    }

    public static String toBaseEncoding(String s, int languageid, String fromdb) {
        return s;
        /*
         * try { LanguageComInfo langinfo = new LanguageComInfo(); String
         * encoding = langinfo.getLanguageencode(""+languageid) ;
         * if(encoding.equalsIgnoreCase("UTF-8")&& fromdb.equals("1")) return s ;
         * byte[] target_byte = s.getBytes(encoding); return new
         * String(target_byte , "ISO8859_1"); } catch (Exception ex) { return s; }
         */
    }

    public static String fromBaseEncoding(String s, int languageid) {
        return s;
        /*
         * try { LanguageComInfo langinfo = new LanguageComInfo(); String
         * encoding = langinfo.getLanguageencode(""+languageid) ;
         * if(encoding.equalsIgnoreCase("UTF-8")) return s ; byte[] target_byte =
         * s.getBytes("ISO8859_1"); return new String(target_byte, encoding); }
         * catch (Exception ex) { return s; }
         */
    }

    public static String fromBaseEncoding2(String s, int languageid) {
        return s;
        /*
         * try { LanguageComInfo langinfo = new LanguageComInfo(); String
         * encoding = langinfo.getLanguageencode(""+languageid) ; byte[]
         * target_byte = s.getBytes("ISO8859_1"); return new String(target_byte,
         * encoding); } catch (Exception ex) { return s; }
         */
    }
    
    public static String fromScreenForCpt(String s, int languageid) {
        if (s == null)
            s = null2String(s);
        s = fromBaseEncoding(s, languageid);
        s = toHtmlForCpt(s);
        return s;
    }
    
    public static String toHtmlForCpt(String s) {
        s=StringReplace(s,"<br>",""+'\n');  
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            //以下注释 TD2432 关于字段中含有单引号的问题 董平 2005-11-11 (光棍节 呵呵~),重新加入，处理插入数据插入 by ben 2005-12-29
            if (ch == '\'')
                buf.append("\''");
            else if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            // 刘煜增加针对英文双引号的处理
            else if (ch == '"')
                buf.append("&quot;");
            else if (ch == '\n')
                buf.append("<br>");
//          td7254 增加对空格的处理
            else if (ch == ' ')
                buf.append("&nbsp");
            else
            	buf.append(ch);
        }
        return buf.toString();
    }

	public static String toHtml(String s,boolean f) {
        s=StringReplace(s,"<br>",""+'\n');  
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            //以下注释 TD2432 关于字段中含有单引号的问题 董平 2005-11-11 (光棍节 呵呵~),重新加入，处理插入数据插入 by ben 2005-12-29
            if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            // 刘煜增加针对英文双引号的处理
            else if (ch == '"')
                buf.append("&quot;");
            else if (ch == '\n')
                buf.append("<br>");
            else
            	buf.append(ch);
        }
        return buf.toString();
    }

    public static String toHtml(String s) {
        s=StringReplace(s,"<br>",""+'\n');  
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            //以下注释 TD2432 关于字段中含有单引号的问题 董平 2005-11-11 (光棍节 呵呵~),重新加入，处理插入数据插入 by ben 2005-12-29
            if (ch == '\'')
                buf.append("\''");
            else if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            // 刘煜增加针对英文双引号的处理
            else if (ch == '"')
                buf.append("&quot;");
            else if (ch == '\n')
                buf.append("<br>");
            else
            	buf.append(ch);
        }
        return buf.toString();
    }
    
    public static String toXmlText(String s) {
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];
            if (ch == '\'')
                buf.append("&apos;");
            else if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            else if (ch == '"')
                buf.append("&quot;");
            else
            	buf.append(ch);
        }
        return buf.toString();
    }
    
    public static String toHtmlA(String s) {
      char c[] = s.toCharArray();
      char ch;
      int i = 0;
      StringBuffer buf = new StringBuffer();

      while (i < c.length) {
          ch = c[i++];
          if (ch == '\'')
              buf.append("\''");
          else if (ch == '<')
              buf.append("&lt;");
          else if (ch == '>')
              buf.append("&gt;");
          else if (ch == '"')
              buf.append("&quot;");
          else{
            //如果是换行符和回车符(\n \r)，则将其替换掉。
            if (ch != '\n' && ch != '\r'){
              buf.append(ch);
            }
          }
      }
      return buf.toString();
  }
    
    public static String toExcel(String s) {
        s=StringReplace(s,"<br>",""+'\n');  
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            //以下注释 TD2432 关于字段中含有单引号的问题 董平 2005-11-11 (光棍节 呵呵~),重新加入，处理插入数据插入 by ben 2005-12-29
            if (ch == '\'')
                buf.append("\''");
            else if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            // 刘煜增加针对英文双引号的处理
            else if (ch == '"')
                buf.append("&quot;");
            else
            	buf.append(ch);
        }
        return buf.toString();
    }

	public static String forHtml(String s) {
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            else if (ch == '"')
                buf.append("&quot;");
            else if (ch == '\n')
                buf.append("<br>");
            else if (ch == '\r')
                buf.append("");
            else
            	buf.append(ch);
        }
        return buf.toString();
    }

    public static String toHtmltextarea(String s) {
        s=StringReplace(s,"<br>",""+'\n');
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            //以下注释 TD2432 关于字段中含有单引号的问题 董平 2005-11-11 (光棍节 呵呵~),重新加入，处理插入数据插入 by ben 2005-12-29
            if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            // 刘煜增加针对英文双引号的处理
            else if (ch == '"')
                buf.append("&quot;");
            else
            	buf.append(ch);
        }
        return buf.toString();
    }

    public static String spacetoHtml(String s) {
        s=StringReplace(s,"<br>",""+'\n');  
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            //以下注释 TD2432 关于字段中含有单引号的问题 董平 2005-11-11 (光棍节 呵呵~),重新加入，处理插入数据插入 by ben 2005-12-29
            if (ch == '\'')
                buf.append("\''");
            else if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            // 刘煜增加针对英文双引号的处理
            else if (ch == '"')
                buf.append("&quot;");
            else if (ch == '\n')
                buf.append("<br>");
            else if (ch == ' ')
			    buf.append("&nbsp;");
            else
            	buf.append(ch);
        }
        return buf.toString();
    }
    public static String toHtml10(String s)
    {  if (s == null)
        s = null2String(s);
    	String tempString=s;
    	tempString=StringReplace(tempString,"<br>",""+'\n');  
    	tempString=StringReplace(tempString,"\'","\''");
    	tempString=StringReplace(tempString,"<","&lt;");
    	tempString=StringReplace(tempString,">'","&gt;'");
    	tempString=StringReplace(tempString,"\"","&quot;");
    	tempString=StringReplace(tempString,"\n","<br>");
    	try
    	{
    	return tempString;
    	}
    	catch (Exception ee)
    	{
    		return tempString;
    	}
    	
    }
    
    public static String toHtmlVoting(String s)
    {  if (s == null)
        s = null2String(s);
    	String tempString=s;
    	tempString=StringReplace(tempString,"<br>",""+'\n');  
    	tempString=StringReplace(tempString,"\'","\'");
    	tempString=StringReplace(tempString,"<","&lt;");
    	tempString=StringReplace(tempString,">'","&gt;'");
    	tempString=StringReplace(tempString,"\"","&quot;");
    	tempString=StringReplace(tempString,"\n","<br>");
    	try
    	{
    	return tempString;
    	}
    	catch (Exception ee)
    	{
    		return tempString;
    	}
    	
    }


    public static String toHtml100(String s)
    {  if (s == null)
        s = null2String(s);
    	String tempString=s;
    	///tempString=StringReplace(tempString,"<br>",""+'\n');  
    	tempString=StringReplace(tempString,"\'","\''");
    	//tempString=StringReplace(tempString,"\"","&quot;");
    	//tempString=StringReplace(tempString,"\n","<br>");
    	try
    	{
    	return tempString;
    	}
    	catch (Exception ee)
    	{
    		return tempString;
    	}
    	
    }

    public static String toHtml2(String s) {
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];
            if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            else if (ch == '"')
                buf.append("&quot;");
            else if (ch == '\n')
                buf.append("<br>");
            else if (ch == '\r')
                buf.append("<br>");            
            else
                buf.append(ch);
        }
        return buf.toString();
    }

    public static String toHtml3(String s) {
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];
            if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            else if (ch == '"')
                buf.append("&quot;");
            else if (ch == '\n')
                buf.append("<br>");
            else if (ch == '\'')
                buf.append("\\\'");
            else
                buf.append(ch);
        }
        return buf.toString();
    }

    public static String toHtml4(String s) {
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            if (ch == '\'')
                buf.append("\\\'");
            else
                buf.append(ch);
        }
        return buf.toString();
    }

    //this method added xwj
    public static String toHtml5(String s) {
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            if (ch == '\'')
                buf.append("\'");
            else if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            else if (ch == '"')
                buf.append("&quot;");
            else if (ch == '\n')
                buf.append("<br>");
            else
                buf.append(ch);
        }
        return buf.toString();
    }

    //  added by hubo, 2005/09/01
    public static String toHtml6(String s) {
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            if (ch == '\'')
                buf.append("''");
            else if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            else if (ch == '"')
                buf.append("&quot;");
            else if (ch == '\n')
                buf.append("<br>");
            else if (ch == '\r')
                buf.append("<br>");
            else
                buf.append(ch);
        }
        return buf.toString();
    }

	public static String toHtml7(String s) {
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            if (ch == '\'')
                buf.append("’");
            else if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            else if (ch == '"')
                buf.append("”");
            else if (ch == '\n')
                buf.append("<br>");
            else if (ch == '\r')
                buf.append("<br>");
            else
                buf.append(ch);
        }
        return buf.toString();
    }
	public static String toHtml8(String s) {
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            if (ch == '\'')
                buf.append("\\'");
            else if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            else if (ch == '"')
                buf.append("&quot;");
            else if (ch == '\\')
                buf.append("\\\\");  
            else if (ch == '?')
                buf.append("\\?");  
            else
                buf.append(ch);
        }
        return buf.toString();
    }
    public static String toHtmlForSplitPage(String s) {
        char c[] = s.toCharArray();
        char ch;
        int i = 0;
        StringBuffer buf = new StringBuffer();

        while (i < c.length) {
            ch = c[i++];

            if (ch == '\'')
                buf.append("\\\'");
            else if (ch == '<')
                buf.append("&lt;");
            else if (ch == '>')
                buf.append("&gt;");
            else if (ch == '&')
                buf.append("&amp;");            
            else
                buf.append(ch);
        }
        return buf.toString();
    }


	public static String toSqlForSplitPage(String s) {
       
	 s=StringReplace(s,"\\'", "'");  
   	 s=StringReplace(s,"&lt;", "<");  
   	 s=StringReplace(s,"&gt;", ">");
   	 s=StringReplace(s,"&amp;", "&");
   	 return s;
    }

    public static String toHtmlMode(String s) {
   	 s=StringReplace(s,"\''", "\'");  
   	 s=StringReplace(s,"&lt;", "<");  
   	 s=StringReplace(s,"&gt;", ">");
   	 s=StringReplace(s,"&quot;", '"'+"");
   	 s=StringReplace(s,"<br>", "\n");
   	 return s;
   }
    
    public static String toHtmlMode2(String s){
    	s = StringReplace(s,"&amp;", "&");
    	s = toHtmlMode(s);
    	s = StringReplace(s,"&apos;", "'");
    	return s;
    }

    public static String fromHtml(String s) {
        return s;

    }

    public static String fromHtmlToEdit(String s) {
        return StringReplace(s, "<br>", "");
    }

    /**
     * 字符串转换   流程页面字符串转换为保存到数据库中的字符串
     *
     * @return String   转换后的字符串
     */
    public static String toHtmlForWorkflow(String s) {
    	if (s == null){
    		s = null2String(s);
    	}
    	
    	String tempString=s;
    	try{
    		//tempString=StringReplace(tempString,"<br>",""+'\n');
    		tempString=StringReplace(tempString,""+'\n',"<br>");    		
    		tempString=StringReplace(tempString,"\'","\''");
    		tempString=StringReplace(tempString,"&nbsp;"," ");

    		return tempString;
    	}catch (Exception ee){
    		return tempString;
    	}
    }    
    
    /**
     * 解决utf-8编码下，&nbsp;会被转换为乱码？的问题
     * 此方法会把乱码再次转换为&nbsp;
     * @return
     */
    public static String htmlFilter4UTF8(String htmlstr) {
        try {
            byte bytes[] = { (byte) 0xC2, (byte) 0xA0 };
            return htmlstr.replaceAll(new String(bytes, "UTF-8"), "&nbsp;");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return htmlstr;
    }
    
    /**
     * 字符串转换   保存到数据库中的字符串转换为流程页面字符串
     *
     * @return String   转换后的字符串
     */
    public static String toScreenForWorkflow(String s) {
     	if (s == null){
     		s = null2String(s);
     	}
     	
     	String tempString=s;
     	List aList = new ArrayList();
     	try{
     		//TD29528 lv start 对链接做特殊处理
    		while(tempString.indexOf("<a ")>-1){
     			String href = tempString.substring(tempString.indexOf("<a "),tempString.indexOf("</a>")+4);
     			tempString = tempString.substring(0,tempString.indexOf("<a "))+"#"+aList.size()+"#" + tempString.substring(tempString.indexOf("</a>")+4);
     			aList.add(href); 			
     		}
    		//TD29528 lv end
     		tempString=StringReplace(tempString,"<br>",""+'\n'); 
     		tempString=StringReplace(tempString,"<","&lt;");
     		tempString=StringReplace(tempString,">","&gt;");
     		tempString=StringReplace(tempString,"\"","&quot;");
     		tempString=StringReplace(tempString," ","&nbsp;");
            tempString=StringReplace(tempString,"\n",""+"<br>");
            //TD29528 lv start 还原链接
            for(int i=0;i<aList.size();i++){
            	tempString = tempString.replaceFirst("#"+i+"#", (String)aList.get(i));
            }
            //TD29528 lv end
     		return tempString;
     	}catch (Exception ee){
     		return tempString;
     	}
     }       
 
    /**
     * 字符串转换   保存到数据库中的字符串转换为表单页面字符串
     * UTF-8编码，如果"" 转换成&nbsp;,将会有？乱码
     * @return String   转换后的字符串
     */
    public static String toScreenForMode(String s) {
     	if (s == null){
     		s = null2String(s);
     	}
     	
     	String tempString=s;
     	List aList = new ArrayList();
     	try{
     		//TD29528 lv start 对链接做特殊处理
    		while(tempString.indexOf("<a ")>-1){
     			String href = tempString.substring(tempString.indexOf("<a "),tempString.indexOf("</a>")+4);
     			tempString = tempString.substring(0,tempString.indexOf("<a "))+"#"+aList.size()+"#" + tempString.substring(tempString.indexOf("</a>")+4);
     			aList.add(href); 			
     		}
    		//TD29528 lv end
     		tempString=StringReplace(tempString,"<br>",""+'\n'); 
     		tempString=StringReplace(tempString,"<","&lt;");
     		tempString=StringReplace(tempString,">","&gt;");
     		tempString=StringReplace(tempString,"\"","&quot;");
     		tempString=StringReplace(tempString," ","&nbsp;");
            tempString=StringReplace(tempString,"\n",""+"<br>");
            //TD29528 lv start 还原链接
            for(int i=0;i<aList.size();i++){
            	tempString = tempString.replaceFirst("#"+i+"#", (String)aList.get(i));
            }
            //TD29528 lv end
     		return tempString;
     	}catch (Exception ee){
     		return tempString;
     	}
     }  
    
     /**
      * 字符串转换   保存到数据库中的字符串转换为流程页面字符串   只读专用
      *
      * @return String   转换后的字符串
      */
      public static String toScreenForWorkflowReadOnly(String s) {
      	if (s == null){
      		s = null2String(s);
      	}
      	
      	String tempString=s;
      	try{
      		tempString=StringReplace(tempString,""+'\n',"<br>"); 
      		tempString=StringReplace(tempString,"\"","&quot;");
      		tempString=StringReplace(tempString," ","&nbsp;");

      		return tempString;
      	}catch (Exception ee){
      		return tempString;
      	}
      }     
    public static boolean contains(Object a[], Object s) {
        if (a == null || s == null)
            return false;
        for (int i = 0; i < a.length; i++)
            if (a[i] != null && a[i].equals(s))
                return true;
        return false;
    }

    public static String extract(String c, String begin_tag, String end_tag) {
        int begin = begin_tag == null ? 0 : c.indexOf(begin_tag);
        int len = begin_tag == null ? 0 : begin_tag.length();
        int end = end_tag == null ? c.length() : c.indexOf(end_tag, begin + len);

        if (begin == -1) {
            begin = 0;
            len = 0;
        }
        if (end == -1)
            end = c.length();
        return c.substring(begin + len, end);
    }

    public static String remove(String s1, String s2) {
        int i = s1.indexOf(s2);
        int l = s2.length();
        if (i != -1)
            return s1.substring(0, i) + s1.substring(i + l);
        return s1;
    }

    // --------------------------------------------------------------------------
    public static String replaceChar(String s, char c1, char c2) {
        if (s == null)
            return s;

        char buf[] = s.toCharArray();
        for (int i = 0; i < buf.length; i++) {
            if (buf[i] == c1)
                buf[i] = c2;
        }
        return String.valueOf(buf);
    }

    public static boolean isEmail(String s) {
    	if(s==null || "".equals(s)){
    		return false;
    	}
    	if(s.indexOf(";")>=0 || s.indexOf(",")>=0 || s.indexOf(">")>=0 || s.indexOf("<")>=0 || s.indexOf("[")>=0 || s.indexOf("]")>=0 || s.indexOf(")")>=0 || s.indexOf("(")>=0){
    		return false;
    	}
        int pos = 0;
        pos = s.indexOf("@");
        if (pos == -1){
        	return false;
        }
        if(s.substring(pos).indexOf(".")<0){
        	return false;
        }    
        return true;
    }


    public static void swap(Object a[], int i, int j) {
        Object t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static String StringReplace(String sou, String s1, String s2) {
        //int idx = sou.indexOf(s1);
        //if (idx < 0) {
        //    return sou;
        //}
        //return sou.substring(0, idx) + s2 + StringReplace(sou.substring(idx + s1.length()), s1, s2);
        sou = null2String(sou);
        s1 = null2String(s1);
        s2 = null2String(s2);
        try{
            sou = sou.replace(s1, s2);
        }catch(Exception e){
            e.printStackTrace();
        }
        return sou; 
    }

    public static String StringReplaceOnce(String sou, String s1, String s2) {
        int idx = sou.indexOf(s1);
        if (idx < 0) {
            return sou;
        }
        return sou.substring(0, idx) + s2 + sou.substring(idx + s1.length());
    }

    public static String replaceRange(String sentence, String oStart, String oEnd, String rWord, boolean matchCase) {
        int sIndex = -1;
        int eIndex = -1;
        if (matchCase) {
            sIndex = sentence.indexOf(oStart);
        } else {
            sIndex = sentence.toLowerCase().indexOf(oStart.toLowerCase());
        }
        if (sIndex == -1 || sentence == null || oStart == null || oEnd == null || rWord == null) {
            return sentence;
        } else {
            if (matchCase) {
                eIndex = sentence.indexOf(oEnd, sIndex);
            } else {
                eIndex = sentence.toLowerCase().indexOf(oEnd.toLowerCase(), sIndex);
            }
            String newStr = null;
            if (eIndex > -1) {
                newStr = sentence.substring(0, sIndex) + rWord + sentence.substring(eIndex + oEnd.length());
            } else {
                newStr = sentence.substring(0, sIndex) + rWord + sentence.substring(sIndex + oStart.length());
            }
            return replaceRange(newStr, oStart, oEnd, rWord, matchCase);
        }
    }

    // add by wangwei

    public static int getIntValue(String v) {
        return getIntValue(v, -1);
    }

    public static int getIntValue(String v, int def) {
        try {
            return Integer.parseInt(v);
        } catch (Exception ex) {
            return def;
        }
    }

    public static float getFloatValue(String v) {
        return getFloatValue(v, -1);
    }

    public static float getFloatValue(String v, float def) {
        try {
            return Float.parseFloat(v);
        } catch (Exception ex) {
            return def;
        }
    }

    public static double getDoubleValue(String v) {
        return getDoubleValue(v, -1);
    }

    public static double getDoubleValue(String v, double def) {
        try {
            return Double.parseDouble(v);
        } catch (Exception ex) {
            return def;
        }
    }

    //add by xys 20050607
    //取小数点后第几位
    public static String getPointValue(String v) {
        return getPointValue(v, 2);
    }
    //得到整数部分
    public static String getIntValues(String v) {
    	//Double.parseDouble(v);
    	String temp=v;
    	if (v.indexOf(".") == -1) {
    		temp= v;
    	}
    	else
    	{
    		temp = v.substring(0, v.indexOf("."));
    	}
    
        return temp;
    }
    public static String getPointValue(String v, int i) {
        return getPointValue(v, 2, "-1");
    }
	
	//added by hubo,2006-03-17
	public static String getPointValue2(String v, int i) {
        return getPointValue(v, i, "-1");
    }
   //added by ben,2006-03-15
	public static String getPointValue3(String v, int i,String k) {
		String temp = v;
		if (temp.indexOf(".") == -1) 
            temp = temp;
            else
            temp=getPointValue(v, i, k);
		
        return temp;
    }
    public static String getPointValue(String v, int i, String k) {
        try {
            Double.parseDouble(v);
            String temp = v;
            if (v.indexOf("E") != -1) {
                temp = getfloatToString(v);
            }
            if (temp.indexOf(".") == -1) {
                temp = temp + ".";
                for (int j = 0; j < i; j++)
                    temp = temp + "0";
            } else if ((temp.length() - temp.lastIndexOf(".")) <= i) {
                for (int j = 0; j < (i - temp.length() + temp.lastIndexOf(".") + 1); j++)
                    temp = temp + "0";
            } else {
                temp = temp.substring(0, temp.lastIndexOf(".") + i + 1);
            }
            return temp;
        } catch (Exception e) {
            return k;
        }
    }

    //add end
    public static String getFileidIn(String in) {
        return in;
    }

    public static String getFileidOut(String out) {
        return out;
    }

    public static String makeNavbar(int current, int total, int per_page, String action) {
        int begin = 1;
        int border = per_page * 5;
        String prevLink = "";
        String nextLink = "";
        String rslt = "";

        String strNext = "Next";
        String strPrev = "Previous";
        String strStart = "";
        if (action.indexOf("?") < 0)
            strStart = "?start=";
        else
            strStart = "&start=";

        Hashtable ht = new Hashtable();
        ht.put("action", action);
        begin = current + 1;
        int j = 0;
        while (j * border < begin)
            j++;

        begin = (((j - 1) * border) + 1);

        if (current + 1 > border)
            prevLink = "<a href='" + action + strStart + Math.max(1, begin - border) + "'>[ " + strPrev + " " + border
                    + " ]</a>&nbsp;";

        while (begin < (j * border) && begin < total + 1) {
            ht.put("from", String.valueOf(begin));
            ht.put("to", String.valueOf(Math.min(total, begin + per_page - 1)));

            if (current + 1 >= begin && current + 1 <= begin + per_page - 1) {
                rslt += fillValuesToString("[$from - $to]&nbsp;", ht);
            } else
                rslt += fillValuesToString("<a href='$action" + strStart + "$from'>[$from - $to]</a>&nbsp;", ht);
            begin += per_page;
        }

        if (total >= begin)
            nextLink = "&nbsp;<a href='" + action + strStart + begin + "'>[ " + strNext + " "
                    + Math.min(border, total - begin + 1) + " ]</a>";
        return prevLink + rslt + nextLink;
    }
    


	public static String makeNavbar2(int pagenum, int total, int per_page, String action) {
        int begin = 1;
        int current = (pagenum - 1) * per_page + 1;
        int border = per_page * 5;
        String prevLink = "";
        String nextLink = "";
        String rslt = "";

        String strNext = "Next";
        String strPrev = "Previous";
        String strStart = "";
        if (action.indexOf("?") < 0)
            strStart = "?pagenum=";
        else
            strStart = "&pagenum=";

        Hashtable ht = new Hashtable();
        ht.put("action", action);
        begin = current + 1;
        int j = 0;
        while (j * border < begin)
            j++;

        begin = (((j - 1) * border) + 1);

        if (current + 1 > border)
            prevLink = "<a href='" + action + strStart + Math.max(1, (begin - border) / per_page) + "'>[ " + strPrev
                    + " " + border + " ]</a>&nbsp;";

        while (begin < (j * border) && begin < total + 1) {
            ht.put("from", String.valueOf(begin));
            ht.put("to", String.valueOf(Math.min(total, begin + per_page - 1)));
            ht.put("pagenum", String.valueOf((begin + per_page - 1) / per_page));

            if (current + 1 >= begin && current + 1 <= begin + per_page - 1) {
                rslt += fillValuesToString("[$from - $to]&nbsp;", ht);
            } else
                rslt += fillValuesToString("<a href='$action" + strStart + "$pagenum'>[$from - $to]</a>&nbsp;", ht);
            begin += per_page;
        }

        if (total >= begin)
            nextLink = "&nbsp;<a href='" + action + strStart + (begin / per_page + 1) + "'>[ " + strNext + " "
                    + Math.min(border, total - begin + 1) + " ]</a>";
        return prevLink + rslt + nextLink;
    }


    /**
     * 可以订制状态栏 订制的时候可以执行 funcName里指定的函数
     * 
     * @author dongping
     * @param pagenum
     *            当前的页数
     * @param total
     *            记录的总条数
     * @param per_page
     *            每页要放的条数
     * @param action
     *            相当于likeStr
     * @param FuncName
     *            函数名 如:doPostClick()
     * @return
     */
    public static String makeNavbarUseFunc(int pagenum, int total, int per_page, String action, String FuncName) {
        int begin = 1;
        int current = (pagenum - 1) * per_page + 1;
        int border = per_page * 5;
        String prevLink = "";
        String nextLink = "";
        String rslt = "";

        String strNext = "Next";
        String strPrev = "Previous";
        String strStart = "";
        if (action.indexOf("?") < 0)
            strStart = "?pagenum=";
        else
            strStart = "&pagenum=";

        Hashtable ht = new Hashtable();
        ht.put("action", action);
        begin = current + 1;
        int j = 0;
        while (j * border < begin)
            j++;

        begin = (((j - 1) * border) + 1);

        if (current + 1 > border)
            prevLink = "<a href='" + action + strStart + Math.max(1, (begin - border) / per_page) + "' onclick='"
                    + FuncName + "'>[ " + strPrev + " " + border + " ]</a>&nbsp;";

        while (begin < (j * border) && begin < total + 1) {
            ht.put("from", String.valueOf(begin));
            ht.put("to", String.valueOf(Math.min(total, begin + per_page - 1)));
            ht.put("pagenum", String.valueOf((begin + per_page - 1) / per_page));

            if (current + 1 >= begin && current + 1 <= begin + per_page - 1) {
                rslt += fillValuesToString("[$from - $to]&nbsp;", ht);
            } else
                rslt += fillValuesToString("<a href='$action" + strStart + "$pagenum' onclick='" + FuncName
                        + "'>[$from - $to]</a>&nbsp;", ht);
            begin += per_page;
        }

        if (total >= begin)
            nextLink = "&nbsp;<a href='" + action + strStart + (begin / per_page + 1) + "' onclick='" + FuncName
                    + "'>[ " + strNext + " " + Math.min(border, total - begin + 1) + " ]</a>";
        return prevLink + rslt + nextLink;
    }

    // add by wjy
    /**
     * 换页前完成一些其他的功能
     * 
     * @param pagenum
     * @param total
     * @param per_page
     * @param action
     * @return
     */
    public static String makeNavbar3(int pagenum, int total, int per_page, String action) {
        int begin = 1;
        int current = (pagenum - 1) * per_page + 1;
        int border = per_page * 5;
        String prevLink = "";
        String nextLink = "";
        String rslt = "";

        String strNext = "Next";
        String strPrev = "Previous";
        String strStart = "";
        if (action.indexOf("?") < 0)
            strStart = "?pagenum=";
        else
            strStart = "&pagenum=";

        Hashtable ht = new Hashtable();
        ht.put("action", action);
        begin = current + 1;
        int j = 0;
        while (j * border < begin)
            j++;

        begin = (((j - 1) * border) + 1);

        if (current + 1 > border)
            prevLink = "<a href='javascript:changePagePre(\"" + action + strStart
                    + Math.max(1, (begin - border) / per_page) + "\")'>[ " + strPrev + " " + border + " ]</a>&nbsp;";

        while (begin < (j * border) && begin < total + 1) {
            ht.put("from", String.valueOf(begin));
            ht.put("to", String.valueOf(Math.min(total, begin + per_page - 1)));
            ht.put("pagenum", String.valueOf((begin + per_page - 1) / per_page));

            if (current + 1 >= begin && current + 1 <= begin + per_page - 1) {
                rslt += fillValuesToString("[$from - $to]&nbsp;", ht);
            } else
                rslt += fillValuesToString("<a href='javascript:changePageTo(\"$action" + strStart
                        + "$pagenum\")'>[$from - $to]</a>&nbsp;", ht);
            begin += per_page;
        }

        if (total >= begin)
            nextLink = "&nbsp;<a href='javascript:changePageNext(\"" + action + strStart + (begin / per_page + 1)
                    + "\")'>[ " + strNext + " " + Math.min(border, total - begin + 1) + " ]</a>";
        return prevLink + rslt + nextLink;
    }

    public static String makeNavbarReverse(int current, int total, int per_page, String action) {
        int border = per_page * 5;
        String prevLink = "";
        String nextLink = "";
        String rslt = "";
        int begin = current + 1;
        Hashtable ht = new Hashtable();
        ht.put("action", action);
        String strNext = "Next";
        String strPrev = "Previous";
        String strStart = "";
        if (action.indexOf("?") < 0)
            strStart = "?start=";
        else
            strStart = "&start=";

        int j = 0;
        while (j * border < begin)
            j++;
        begin = ((j - 1) * border) + 1;
        if (begin > border)
            prevLink = "<a href=" + action + strStart + Math.max(1, begin - border) + ">[ " + strNext + " " + border
                    + " ]</a>&nbsp;";
        current++;
        for (int i = 0; i < per_page && begin <= total; i++) {
            ht.put("from", String.valueOf(total - begin + 1));
            ht.put("to", String.valueOf(Math.max(1, total - begin + 1 + 1 - per_page)));
            if (current >= begin && current <= begin + per_page - 1) {
                rslt += fillValuesToString("[$from - $to]&nbsp;", ht);
            } else
                rslt += fillValuesToString("<a href='$action" + strStart + String.valueOf(begin)
                        + "'>[ $from - $to ]</a>&nbsp;", ht);
            begin += per_page;
        }
        if (total > begin)
            nextLink = "&nbsp;<a href=" + action + strStart + String.valueOf(begin) + ">[ " + strPrev + " "
                    + Math.min(total - begin + 1, 100) + " ]</a>";
        return prevLink + rslt + nextLink;
    }

    public static String fillValuesToString(String str, Hashtable ht) {
        char VARIABLE_PREFIX = '$';
        char TERMINATOR = '\\';

        if (str == null || str.length() == 0 || ht == null)
            return str;

        char s[] = str.toCharArray();
        char ch, i = 0;
        String vname;
        StringBuffer buf = new StringBuffer();

        ch = s[i];
        while (true) {
            if (ch == VARIABLE_PREFIX) {
                vname = "";
                if (++i < s.length)
                    ch = s[i];
                else
                    break;
                while (true) {
                    if (ch != '_' && ch != '-' && !Character.isLetterOrDigit(ch))
                        break;
                    vname += ch;
                    if (++i < s.length)
                        ch = s[i];
                    else
                        break;
                }

                if (vname.length() != 0) {
                    String vval = (String) ht.get(vname);
                    if (vval != null)
                        buf.append(vval);
                }
                if (vname.length() != 0 && ch == VARIABLE_PREFIX)
                    continue;
                if (ch == TERMINATOR) {
                    if (++i < s.length)
                        ch = s[i];
                    else
                        break;
                    continue;
                }
                if (i >= s.length)
                    break;
            }

            buf.append(ch);
            if (++i < s.length)
                ch = s[i];
            else
                break;
        }
        return buf.toString();
    }

    public static String fillValuesToString2(String str, Hashtable ht) {
        char VARIABLE_PREFIX = '$';
        char TERMINATOR = '\\';

        if (str == null || str.length() == 0 || ht == null)
            return str;

        char s[] = str.toCharArray();
        char ch, i = 0;
        String vname;
        StringBuffer buf = new StringBuffer();

        ch = s[i];
        while (true) {
            if (ch == VARIABLE_PREFIX) {
                vname = "";
                if (++i < s.length)
                    ch = s[i];
                else
                    break;
                while (true) {
                    if (!Character.isLetterOrDigit(ch))
                        break;
                    vname += ch;
                    if (++i < s.length)
                        ch = s[i];
                    else
                        break;
                }

                if (vname.length() != 0) {
                    String vval = (String) ht.get(vname);
                    if (vval != null)
                        buf.append(vval);
                }
                if (vname.length() != 0 && ch == VARIABLE_PREFIX)
                    continue;
                if (ch == TERMINATOR) {
                    if (++i < s.length)
                        ch = s[i];
                    else
                        break;
                    continue;
                }
                if (i >= s.length)
                    break;
            }

            buf.append(ch);
            if (++i < s.length)
                ch = s[i];
            else
                break;
        }
        return buf.toString();
    }

    public static char getSeparator() {
        return 2;
    }

    public static String addTime(String time1, String time2) {
        if (time1.equals("") || time2.equals("")) {
            return "00:00";
        } else {
            ArrayList timearray1 = TokenizerString(time1, ":");
            ArrayList timearray2 = TokenizerString(time2, ":");
            int hour1;
            int hour2;
            int min1;
            int min2;
            int hour;
            int min;
            hour1 = getIntValue((String) timearray1.get(0));
            min1 = getIntValue((String) timearray1.get(1));
            hour2 = getIntValue((String) timearray2.get(0));
            min2 = getIntValue((String) timearray2.get(1));
            if ((min1 + min2) >= 60) {
                hour = hour1 + hour2 + 1;
                min = min1 + min2 - 60;
            } else {
                hour = hour1 + hour2;
                min = min1 + min2;
            }
            if (hour < 10) {
                if (min < 10)
                    return "0" + hour + ":" + "0" + min;
                else
                    return "0" + hour + ":" + "" + min;
            } else {
                if (min < 10)
                    return "" + hour + ":" + "0" + min;
                else
                    return "" + hour + ":" + "" + min;
            }
        }
    }

    public static String subTime(String time1, String time2) {
        if (time1.equals("") || time2.equals("")) {
            return "00:00";
        } else {
            ArrayList timearray1 = TokenizerString(time1, ":");
            ArrayList timearray2 = TokenizerString(time2, ":");
            int hour1;
            int hour2;
            int min1;
            int min2;
            int hour;
            int min;
            hour1 = getIntValue((String) timearray1.get(0));
            min1 = getIntValue((String) timearray1.get(1));
            hour2 = getIntValue((String) timearray2.get(0));
            min2 = getIntValue((String) timearray2.get(1));
            if ((min1 - min2) < 0) {
                hour = hour1 - hour2 - 1;
                min = min1 - min2 + 60;
            } else {
                hour = hour1 - hour2;
                min = min1 - min2;
            }
            if (hour < 10) {
                if (min < 10)
                    return "0" + hour + ":" + "0" + min;
                else
                    return "0" + hour + ":" + "" + min;
            } else {
                if (min < 10)
                    return "" + hour + ":" + "0" + min;
                else
                    return "" + hour + ":" + "" + min;
            }
        }
    }

    public static String getFloatStr(String str, int dimlen) { // 获得每dimlen位的逗号
        int dicimalindex = str.indexOf(".");
        String decimalstr = "";
        if (dicimalindex != -1)
            decimalstr = extract(str, ".", null);
        String intstr = extract(str, null, ".");
        if (intstr.length() < (dimlen + 1))
            return str;
        String beginstr = "";
        int thebeginlen = intstr.length() % dimlen;
        beginstr = intstr.substring(0, thebeginlen);
        intstr = intstr.substring(thebeginlen);
        int intstrcount = intstr.length() / dimlen;

        for (int i = 0; i < intstrcount; i++) {
            if (beginstr.equals("") || beginstr.equals("-")) {
                beginstr += intstr.substring(0, dimlen);
                intstr = intstr.substring(dimlen);
            } else {
                beginstr += "," + intstr.substring(0, dimlen);
                intstr = intstr.substring(dimlen);
            }
        }
        if (dicimalindex != -1)
            return beginstr + "." + decimalstr;
        else
            return beginstr;
    }

    public static String getRandom() {
        int randomInt = 1000000000 + random.nextInt(1000000000);
        while (randomInt == 0) {
            randomInt = 1000000000 + random.nextInt(1000000000);
        }
        return String.valueOf(randomInt);
    }

    public static int getPerpageLog() {
        return 10;
    }

    public static String getPortalPassword() {
        String psd = "";
        char c;
        int i;
        int isnum = 0;
        for (int j = 0; j < 8; j++) {
            if (isnum == 0) {
                isnum = 1;
                c = (char) (Math.random() * 26 + 'a');
                psd += c;
            } else {
                isnum = 0;
                c = (char) (Math.random() * 10 + '0');
                psd += c;
            }
        }
        return psd;
    }


    public static String getCharString(int preint) //将数字转换为ABCD的ACCLE 字母
    {
        String thechar = "";
        int charlen=getchars(preint,26,0);
        for(int i=charlen;i>0;i--){
            if(i>1){
                int temp=(new java.math.BigDecimal(Math.pow(26, (i - 1))).intValue());
                int tempint = preint - temp;
                int modint = tempint % temp;
                int scaleint =tempint / temp;
                preint=preint- temp;
                if (modint != 0)  scaleint++;
                if(scaleint>26){
                    scaleint=scaleint%26;
                    if(scaleint==0) scaleint=26;
                }
                thechar += (char) (scaleint + 64);
            }else{
                int modint =preint;
                if(preint>26)  modint=preint % 26;
                if(modint==0) modint=26;
                thechar += (char) (modint + 64);
            }
        }
        return thechar;
    }

    public static int getchars(int value1,int value2,int modvalue){
        int i=1;
        int lengths=value1/value2;
        int mod=value1%value2;
        if(lengths>0){
            if(lengths>value2){
                if(mod==0){
                    i+=getchars(lengths,value2,lengths/value2);
                }else{
                    i+=getchars(lengths,value2,0);
                }
            }else{
                if(lengths>1 || mod>modvalue) i++;
            }
        }
        return i;
    }


    public static int getCharInt(String prestr) //将ABCD的ACCLE 字母转换为数字
    {
        int thelength = prestr.length();
        int theint = 0;
        for(int i=0;i<thelength;i++) {
            theint += ((int) prestr.charAt(thelength-(i+1)) - 64)*(new java.math.BigDecimal(Math.pow(26, i)).intValue());
        }
        return theint;
    }

    public static String getfloatToString(String value) {
		boolean isNegNum =false;
        if(value.indexOf("-")!=-1){  //td49359 by sjh 当传入的数据位负数时需先去负号，返回时需添加上负号
        	isNegNum=true;
        	value=value.substring(1,value.length());
        }
        int index = value.indexOf("E");
        if (index == -1)
            return value;

        int num = Integer.parseInt(value.substring(index + 1, value.length()));
        value = value.substring(0, index);
        index = value.indexOf(".");
        value = value.substring(0, index) + value.substring(index + 1, value.length());
        String number = value;
        if (value.length() <= num) {
            for (int i = 0; i < num - value.length()+1; i++) { //td49359 by sjh 长度取值应该是取小数点后的数值长度而不是包含小数点前的数值
                number += "0";
            }
        } else {
            number = number.substring(0, num + 1) + "." + number.substring(num + 1) + "0";
        }
		if(isNegNum)number="-"+number;
        return number;
    }

    public static String getRequestHost(HttpServletRequest request) {
        return (null2String(request.getHeader("Host"))).trim();
    }

    public static int dayDiff(String startdate, String enddate) {
        int datediff = 0;
        if (startdate.equals("") || startdate == null || startdate.length() < 10 || enddate.equals("")
                || enddate == null || enddate.length() < 10) {
            return 0;
        }

        String fromtempdate = "";
        String totempdate = "";
        int derictive = 1;

        if (startdate.compareTo(enddate) <= 0) {
            fromtempdate = startdate;
            totempdate = enddate;
        } else {
            fromtempdate = enddate;
            totempdate = startdate;
            derictive = -1;
        }

        int fromyear = getIntValue(fromtempdate.substring(0, 4));
        int frommonth = getIntValue(fromtempdate.substring(5, 7));
        int fromday = getIntValue(fromtempdate.substring(8, 10));

        Calendar thedate = Calendar.getInstance();

        thedate.set(fromyear, frommonth - 1, fromday);

        while (fromtempdate.compareTo(totempdate) <= 0) {
            datediff++;
            thedate.add(Calendar.DATE, 1);
            fromtempdate = Util.add0(thedate.get(Calendar.YEAR), 4) + "-"
                    + Util.add0(thedate.get(Calendar.MONTH) + 1, 2) + "-"
                    + Util.add0(thedate.get(Calendar.DAY_OF_MONTH), 2);
        }

        return derictive * datediff;
    }

    public static int monthDiff(String startdate, String enddate) {
        int monthdiff = 0;
        if (startdate.equals("") || startdate == null || startdate.length() < 10 || enddate.equals("")
                || enddate == null || enddate.length() < 10) {
            return 0;
        }
        int year1 = getIntValue(startdate.substring(0, 4));
        int month1 = getIntValue(startdate.substring(5, 7)) - 1;
        int year2 = getIntValue(enddate.substring(0, 4));
        int month2 = getIntValue(enddate.substring(5, 7)) - 1;

        monthdiff = month2 - month1;
        monthdiff += 12 * (year2 - year1);

        return monthdiff;
    }

    public static int monthDiff2(String startdate, int month2) {
        int monthdiff = 0;
        if (startdate.equals("") || startdate == null || startdate.length() < 10) {
            return 0;
        }

        int month1 = getIntValue(startdate.substring(5, 7));

        monthdiff = month2 - month1;

        return monthdiff;
    }

    public static int yearDiff(String startdate, String enddate) {
        int yeardiff = 0;
        if (startdate.equals("") || startdate == null || startdate.length() < 10 || enddate.equals("")
                || enddate == null || enddate.length() < 10) {
            return 0;
        }
        int year1 = getIntValue(startdate.substring(0, 4));
        int year2 = getIntValue(enddate.substring(0, 4));

        yeardiff = year2 - year1;

        return yeardiff;
    }

    public static int yearDiff2(String startdate, int year2) {
        int yeardiff = 0;
        if (startdate.equals("") || startdate == null || startdate.length() < 10) {
            return 0;
        }
        int year1 = getIntValue(startdate.substring(0, 4));

        yeardiff = year2 - year1;

        return yeardiff;
    }

    /**
     * 时间之差， 返回相差的时间（分钟）
     *
     * @para time1 时间，格式为 hh:mm
     * @para time2 时间，格式为 hh:mm
     * @return time1-time2 返回相差的分钟数量
     */
    public static int timediff1(String time1, String time2) {
        // 如果输入格式不正确， 返回 0
        if (time1.length() != 5 || time2.length() != 5)
            return 0;

        Calendar tempdatecal = Calendar.getInstance();
        int tempdiffinhour1 = Util.getIntValue(time1.substring(0, 2), 0);
        int tempdiffinminitus1 = Util.getIntValue(time1.substring(3, 5), 0);
        int tempdiffinhour2 = Util.getIntValue(time2.substring(0, 2), 0);
        int tempdiffinminitus2 = Util.getIntValue(time2.substring(3, 5), 0);

        tempdatecal.set(tempdatecal.get(Calendar.YEAR), tempdatecal.get(Calendar.MONTH), tempdatecal
                .get(Calendar.DAY_OF_MONTH), tempdiffinhour1, tempdiffinminitus1);

        Date thedate1 = tempdatecal.getTime();
        long thetime1 = thedate1.getTime();

        tempdatecal.set(tempdatecal.get(Calendar.YEAR), tempdatecal.get(Calendar.MONTH), tempdatecal
                .get(Calendar.DAY_OF_MONTH), tempdiffinhour2, tempdiffinminitus2);

        Date thedate2 = tempdatecal.getTime();
        long thetime2 = thedate2.getTime();

        int theaps = (new Long((thetime1 - thetime2) / 60000)).intValue();

        return theaps;
    }

    public static int timediff2(String date1, String time1, String date2, String time2) {
        // 如果输入格式不正确， 返回 0
        if (date1.length() != 10 || time1.length() != 5 || date2.length() != 10 || time2.length() != 5)
            return 0;

        Calendar tempdatecal = Calendar.getInstance();
        int tempyear1 = Util.getIntValue(date1.substring(0, 4));
        int tempmonth1 = Util.getIntValue(date1.substring(5, 7));
        int tempday1 = Util.getIntValue(date1.substring(8, 10));
        int tempyear2 = Util.getIntValue(date2.substring(0, 4));
        int tempmonth2 = Util.getIntValue(date2.substring(5, 7));
        int tempday2 = Util.getIntValue(date2.substring(8, 10));
        int tempdiffinhour1 = Util.getIntValue(time1.substring(0, 2), 0);
        int tempdiffinminitus1 = Util.getIntValue(time1.substring(3, 5), 0);
        int tempdiffinhour2 = Util.getIntValue(time2.substring(0, 2), 0);
        int tempdiffinminitus2 = Util.getIntValue(time2.substring(3, 5), 0);

        tempdatecal.set(tempyear1, tempmonth1 - 1, tempday1, tempdiffinhour1, tempdiffinminitus1);

        Date thedate1 = tempdatecal.getTime();
        long thetime1 = thedate1.getTime();

        tempdatecal.set(tempyear2, tempmonth2 - 1, tempday2, tempdiffinhour2, tempdiffinminitus2);

        Date thedate2 = tempdatecal.getTime();
        long thetime2 = thedate2.getTime();

        int theaps = (new Long((thetime1 - thetime2) / 60000)).intValue();

        return theaps;
    }

    public static String makeNavbar4(int current, int total, int per_page, String action) {
        int begin = 1;
        int border = per_page * 5;
        String prevLink = "";
        String nextLink = "";
        String rslt = "";

        String strNext = "Next";
        String strPrev = "Previous";
        String strStart = "";
        if (action.indexOf("?") < 0)
            strStart = "?start=";
        else
            strStart = "&start=";

        Hashtable ht = new Hashtable();
        ht.put("action", action);
        begin = current + 1;
        int j = 0;
        while (j * border < begin)
            j++;

        begin = (((j - 1) * border) + 1);

        if (current + 1 > border)
            prevLink = "<a href='" + action + strStart + Math.max(1, begin - border) + "'> " + strPrev + " " + border
                    + " </a>&nbsp;";

        while (begin < (j * border) && begin < total + 1) {
            int tempbegin = begin / per_page + 1;
            ht.put("from", String.valueOf(tempbegin));
            ht.put("linkfrom", String.valueOf(begin));

            if (current + 1 >= begin && current + 1 <= begin + per_page - 1) {
                rslt += fillValuesToString(" $from &nbsp; ", ht);
            } else
                rslt += fillValuesToString(" <a href='$action" + strStart + "$linkfrom'>$from</a> &nbsp; ", ht);
            begin += per_page;
        }

        if (total >= begin)
            nextLink = "&nbsp;<a href='" + action + strStart + begin + "'> " + strNext + " "
                    + Math.min(border, total - begin + 1) + " </a>";
        return prevLink + rslt + nextLink;
    }

    /**
     * @author dongping
     * @param value
     * @return
     */
    public static String useSpecialTreat(String transmethod, String value) throws Exception {
        return useSpecialTreat(transmethod, value, null);
    }

    /**
     * @author dongping
     * @param value
     * @return
     */
    public static String useSpecialTreat(String transmethod, String value, String value2) throws Exception {
        String returnStr = "";
        try {
            if (transmethod == null || "".equals(transmethod))
                return value;
            int pos = transmethod.lastIndexOf(".");

            if (pos != -1) {
                String transTempClass = transmethod.substring(0, pos);
                String transTempMethod = transmethod.substring(pos + 1);

                Class tempClass = Class.forName(transTempClass);
                Method tempMethod = null;
                Constructor ct = tempClass.getConstructor(null);
                if (value2 == null) {
                    tempMethod = tempClass.getMethod(transTempMethod, new Class[] { String.class });
                    returnStr = (String) tempMethod.invoke(ct.newInstance(null), new String[] { value });
                } else {
                    tempMethod = tempClass.getMethod(transTempMethod, new Class[] { String.class, String.class });
                    returnStr = (String) tempMethod.invoke(ct.newInstance(null), new String[] { value, value2 });
                }
                return returnStr;
            } else {
                throw new Exception("没有指定方法名");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static ArrayList useSpecialTreatArrayList(String transmethod, String value) throws Exception {
        return useSpecialTreatArrayList(transmethod, value, null, null);
    }

    public static ArrayList useSpecialTreatArrayList(String transmethod, String value, String value2) throws Exception {
        return useSpecialTreatArrayList(transmethod, value, value2, null);
    }

    public static ArrayList useSpecialTreatArrayList(String transmethod, String value, String value2, String value3)
            throws Exception {
        ArrayList returnList = new ArrayList();

        if (transmethod == null || "".equals(transmethod))
            return returnList;
        int pos = transmethod.lastIndexOf(".");

        if (pos != -1) {
            String transTempClass = transmethod.substring(0, pos);
            String transTempMethod = transmethod.substring(pos + 1);

            Class tempClass = Class.forName(transTempClass);
            Method tempMethod = null;
            Constructor ct = tempClass.getConstructor(null);
            if (value2 == null && value3 == null) {
                tempMethod = tempClass.getMethod(transTempMethod, new Class[] { String.class });
                returnList = (ArrayList) tempMethod.invoke(ct.newInstance(null), new String[] { value });
            } else if (value2 != null && value3 == null) {
                tempMethod = tempClass.getMethod(transTempMethod, new Class[] { String.class, String.class });
                returnList = (ArrayList) tempMethod.invoke(ct.newInstance(null), new String[] { value, value2 });
            } else if (value2 != null && value3 != null) {
                tempMethod = tempClass.getMethod(transTempMethod, new Class[] { String.class, String.class,
                        String.class });
                returnList = (ArrayList) tempMethod
                        .invoke(ct.newInstance(null), new String[] { value, value2, value3 });
            }
            return returnList;
        } else {
            throw new Exception("没有指定方法名");
        }

    }

    // obj[] Array -> ArrayList
    public static ArrayList arrayToArrayList(Object[] srcObjs){
        ArrayList targetList = new ArrayList();
        if (srcObjs==null) return targetList;
        for (int i=0;i<srcObjs.length;i++){
            targetList.add(srcObjs[i]);
        }
        return targetList;
    }


    /**
	 * 替换字符串
	 * @param strSource
	 * @param strFrom
	 */
	public static String replaceString(String strSource, String strFrom, String strTo) {
		if (strSource == null) {
			return strSource;
		}
		int i = 0;
		if ((i = strSource.indexOf(strFrom, i)) >= 0 && strTo != null) {
			char[] cSrc = strSource.toCharArray();
			char[] cTo = strTo.toCharArray();
			int len = strFrom.length();
			StringBuffer buf = new StringBuffer(cSrc.length);
			buf.append(cSrc, 0, i).append(cTo);
			i += len;
			int j = i;
			while ((i = strSource.indexOf(strFrom, i)) > 0) {
				buf.append(cSrc, j, i - j).append(cTo);
				i += len;
				j = i;
			}
			buf.append(cSrc, j, cSrc.length - j);
			return buf.toString();
		}
		return strSource;
	}



    /**
     * Description:得到指定长度的字符串
     * @param str 源字符串
     * @param v  长度
     * @return  指定长度的字符串
     */
    public static String getSubStr(String str,int v)
    {
    	int len=str.length();

    	if (len<v) return str;
    	else return str.substring(0,v-1)+"...";

    }
    /*
     * Description:四舍五入
     * @param scale 精度
     * */
    public static String round(String s, int scale) {
    	try {
    		String sRound = s;
	        if (s.indexOf(".")==-1) {
	        	sRound = s;
	        } else {
	        	sRound = ""+round2(s, scale);
	        }
    		BigDecimal bRound = new BigDecimal(sRound);
    		bRound.setScale(scale);
    		return bRound.toPlainString();
	        //if (s.indexOf(".")==-1) return s;
	        //else
	        //return ""+round2(s, scale);
	    } catch (Exception e) {
	        return s;
	    }

    }
    /*
     * Description:四舍五入
     * @param scale 精度
     * */
    public static double round2(String s, int scale) {
        long temp=1;
        //if (s.indexOf(".")==-1) return s;
        double d=getDoubleValue(s);
        for (int i=scale; i>0; i--) {
                temp*=10;
        }
        d*=temp;
        long dl= Math.round(d);
        return (double)(dl)/temp;

    }

    /**
     * 将输入日期时间增加减去指定秒钟
     * @param date 输入日期
     * @param time 输入时间
     * @return List[0]:日期 List[1]:时间
     */
    public static List processTimeBySecond(String date, String time, int processSecond)
    {
        Calendar calendar = Calendar.getInstance();

        int year = 0000;
        int month = 00;
        int day = 00;
        int hour = 00;
        int minute = 00;
        int second = 00;
        int reduceDay = 0;
        int reduceHour = 0;
        int reduceMin = 0;
        int reduceSec = 0;

        if(!"".equals(date) && null != date)
        {
            List dateList = Util.TokenizerString(date, "-");
            if(0 < dateList.size())
            {
                year = Integer.parseInt((String) dateList.get(0));
            }
            if(1 < dateList.size())
            {
                month = Integer.parseInt((String) dateList.get(1));
            }
            if(2 < dateList.size())
            {
                day = Integer.parseInt((String) dateList.get(2));
            }
        }
        if(!"".equals(time) && null != time)
        {
            List timeList = Util.TokenizerString(time, ":");
            if(0 < timeList.size())
            {
                hour = Integer.parseInt((String) timeList.get(0));
            }
            if(1 < timeList.size())
            {
                minute = Integer.parseInt((String) timeList.get(1));
            }
            if(2 < timeList.size())
            {
                second = Integer.parseInt((String) timeList.get(2));
            }
        }

        List dateTimeResult = new ArrayList();
        String dateResult = "";
        String timeResult = "";

        calendar.set(year, month - 1, day, hour, minute, second);

        if(processSecond / 86400 >= 1)
        //减少时间大于1天
        {
            reduceDay = processSecond / 86400;
            processSecond = processSecond % 86400;
        }
        if(processSecond / 3600 >= 1)
        //减少时间大于1小时
        {
            reduceHour = processSecond / 3600;
            processSecond = processSecond % 3600;
        }
        if(processSecond / 60 >= 1)
        //减少时间大于1分钟
        {
            reduceMin = processSecond / 60;
            processSecond = processSecond % 60;
        }
        reduceSec = processSecond;


        calendar.add(Calendar.DATE, reduceDay);
        calendar.add(Calendar.HOUR, reduceHour);
        calendar.add(Calendar.MINUTE, reduceMin);
        calendar.add(Calendar.SECOND, reduceSec);

        dateResult = Util.add0(calendar.get(Calendar.YEAR), 4) + "-" +
                             Util.add0(calendar.get(Calendar.MONTH) + 1, 2) + "-" +
                             Util.add0(calendar.get(Calendar.DAY_OF_MONTH), 2);
        timeResult = Util.add0(calendar.getTime().getHours(), 2) + ":" +
                             Util.add0(calendar.getTime().getMinutes(), 2) + ":" +
                             Util.add0(calendar.getTime().getSeconds(), 2);
        dateTimeResult.add(dateResult);
        dateTimeResult.add(timeResult);

        return dateTimeResult;
    }

    /**
     * 将输入时间增加减去指定秒钟
     * @param time 输入时间
     * @return 时间
     */
    public static String processTimeBySecond(String time, int processSecond)
    {
        Calendar calendar = Calendar.getInstance();

        int hour = 00;
        int minute = 00;
        int second = 00;

        String timeResult = "";

        if(!"".equals(time) && null != time)
        {
            List timeList = Util.TokenizerString(time, ":");
            if(0 < timeList.size())
            {
                hour = Integer.parseInt((String) timeList.get(0));
            }
            if(1 < timeList.size())
            {
                minute = Integer.parseInt((String) timeList.get(1));
            }
            if(2 < timeList.size())
            {
                second = Integer.parseInt((String) timeList.get(2));
            }
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        calendar.add(Calendar.SECOND, processSecond);

        timeResult = Util.add0(calendar.getTime().getHours(), 2) + ":" +
                             Util.add0(calendar.getTime().getMinutes(), 2) + ":" +
                             Util.add0(calendar.getTime().getSeconds(), 2);

        return timeResult;
    }

   public static void main(String[] args)
    {
        //List list = Util.processTimeBySecond("2006-01-01", "15:15:00", -1);
	   System.out.println(numtochinese("340345339.80"));
        //System.out.println(list.get(0));
        //System.out.println(list.get(1));
    }

    /**
     * @param  str 指定字符串
     * @return 得到字符串的字节数
     */
    public static int length2(String str){
        return str.getBytes().length;
    }


    /**
     * 创建随机字符串
     * @param length
     * @return
     */
    public static String passwordBuilder(int length)
      {
        String str = "";
        String[] arr =
            {
                      "2","3","4","5","6","7","8","9",
                      "a","d","e","f","g","h","i","j",
                      "m","n","r","t","u","y",
                      "A","B","D","E","F","G","H","J",
                      "L","M","N","Q","R","T","Y"
        };

        java.util.Random rd = new java.util.Random();
        while (str.length() < length)
        {
          String temp = arr[rd.nextInt(37)];
          if (str.indexOf(temp) == -1)
          {
            str += temp;
          }
        }
        return str;
      }
    /**
     * 创建随机数字字符串
     * @param length
     * @return
     */
    public static String passwordBuilderNo(int length)
      {
        String str = "";
        String[] arr =
            {
                      "2","3","4","5","6","7","8","9"
        };

        java.util.Random rd = new java.util.Random();
        while (str.length() < length)
        {
          String temp = arr[rd.nextInt(8)];
          if (str.indexOf(temp) == -1)
          {
            str += temp;
          }
        }
        return str;
      }
    /**
     * 创建随机字母字符串
     * @param length
     * @return
     */
    public static String passwordBuilderEn(int length)
      {
        String str = "";
        String[] arr =
            {
                      "a","d","e","f","g","h","i","j",
                      "m","n","r","t","u","y","A","B",
                      "D","E","F","G","H","J","L","M",
                      "N","Q","R","T","Y"
        };

        java.util.Random rd = new java.util.Random();
        while (str.length() < length)
        {
          String temp = arr[rd.nextInt(29)];
          if (str.indexOf(temp) == -1)
          {
            str += temp;
          }
        }
        return str;
      }


	  /**
	  加千分为

	  */
    public   static String milfloatFormat(String args)
	  {		String temp=args;
			String temp1="";
	      if (!args.equals(""))
			{
	    	if (args.indexOf(".") == -1) {
	    		temp= args;
	    	}
	    	else
	    	{
				if(!args.substring(0, args.indexOf(".")).equals("")){
	    		temp = args.substring(0, args.indexOf("."));
				}else{
				temp = "0";
				}
				temp1 =args.substring(args.indexOf(".")+1);
	    	}
	    	double   d  = getDoubleValue(temp);   
	    	DecimalFormat df  = new DecimalFormat("###,###");
	      	if (args.indexOf(".") == -1) {
	      		temp1=""+df.format(d);
	      	}
	      	else
	      	{
				temp1=""+df.format(d)+"."+temp1; 
	      	}
		  }
        return  temp1;
    }    

       /**
	  小写金额转化为大写
	   */
		public static String numtochineseOld(String input){
		String s1="零壹贰叁肆伍陆柒捌玖";
		String s4="分角整圆拾佰仟万拾佰仟亿拾佰仟";
		String temp="";
		String result="";
		if (input==null||input.equals("")) return ""; 
		temp=input.trim(); 
		float f; 
		try{ 
		f=Util.getFloatValue(temp); 

		}catch(Exception e){return "";}
		int len=0; 
		if (temp.indexOf(".")==-1) len=temp.length(); 
		else len=temp.indexOf("."); 
		if(len>s4.length()-3) return "";   //输入字串最大只能精确到仟亿，小数点只能两位！
		int n1,n2=0; 
		String num="";
		String unit="";
        String numtep="";
		for(int i=0;i<temp.length();i++){ 
		if(i>len+2){break;} 
		if(i==len) {continue;} 
		n1= Integer.parseInt(String.valueOf(temp.charAt(i)));
		//int n11=0;
		//if (i<len-1)
		//{
		//	n1=Integer.parseInt(String.valueOf(temp.charAt(i+1))); 
		//	numtep=s1.substring(n11,n11+1); 
		//}
		num=s1.substring(n1,n1+1); 
		
		//if (num.equals(numtep)&&num.equals(s1.substring(0,1))) continue;
		n1=len-i+2; 
		unit=s4.substring(n1,n1+1); 
		result=result.concat(num).concat(unit); 
		} 
		if ((len==temp.length())||(len==temp.length()-1)) result=result.concat("整"); 
		if (len==temp.length()-2) result=result.concat("零分"); 
        //替换无用汉字
		while(result.indexOf("零零") != -1)
		  result = StringReplace(result,"零零", "零");
		 result = StringReplace(result,"零亿", "亿");
		 result = StringReplace(result,"亿万", "亿");
		 result = StringReplace(result,"零万", "万");
		 result = StringReplace(result,"零仟", "零");
		 result = StringReplace(result,"零佰", "零");
		 result = StringReplace(result,"零拾", "零");
		 result = StringReplace(result,"零元", "零");
		 result = StringReplace(result,"零角", "");
		 result = StringReplace(result,"零分", "");
		return result; 
		} 
		
		/**
		小写金额转化为大写
	   */
		public static String numtochinese(String input){
		String temp="";
		String result="";
		if (input==null||input.equals("")) return ""; 
		temp=input.trim(); 
		temp = StringReplace(temp,",","");
		double n;
		try{ 
			n=Util.getDoubleValue(temp); 
		}catch(Exception e){return "";}
		
			 String fraction[] = {"角", "分"};
			 String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
			 String unit[][] = {{"圆", "万", "亿"},
						  {"", "拾", "佰", "仟"}};  
			 String head = n < 0? "负": "";
			 n = Math.abs(n);
			 String s = "";
			 String fractionstr = temp.replaceAll("^\\d+(\\.)?","");
			 for (int i = 0; i < fraction.length; i++) {  
				 //s += (digit[(int)(Math.round(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
				 try{
					 int x = Util.getIntValue(""+fractionstr.charAt(i),0);
					 s += (digit[x]+fraction[i]);
				 }catch(Exception e){
					 
				 }
			 }
			 if(s.equals("零角零分")){
				 s = "";
			 }else if(s.indexOf("零分")!=-1){
				 s = s.substring(0,2);
			 }
			 if(s.length()<1){  
				 s = "整";      
			 }  
			 int integerPart = (int) Math.floor(n);
		
			 for (int i = 0; i < unit[0].length && integerPart > 0; i++) {  
				 String p ="";
				 for (int j = 0; j < unit[1].length && n > 0; j++) {  
					 p = digit[integerPart%10]+unit[1][j] + p;  
					 integerPart = integerPart/10;  
				 }  
				 s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;  
			 }  
			 return head + s.replaceAll("(零.)*零圆", "圆").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零圆整");  
	}
		
		/**
		 * 用于转换字符于FCKEditor中再编辑时需要
		 * @param s as String
		 * @return String
		 */
		public static String encodeAnd(String s){
			return StringReplace(s,"&", "&amp;");
		}
		public static String encodeJS(String s){
			s=StringReplace(s,"\\", "\\\\");
			s=StringReplace(s,"/", "\\/");
			s=StringReplace(s,"'", "\\\'");
			//s=StringReplace(s,"\"", "\\\"");
			s=StringReplace(s,"\n", "\\n");
			s=StringReplace(s,"\r", "\\r");
			return s;
		}
		

		public static String getTxtWithoutHTMLElement (String element){
		      String reg="<[^<0-9a-z|^>]+>";
		      String reg1="<script>[^<|^>]+</script>";
		      return  element.replaceAll(reg1,"").replaceAll(reg,"");
		}		
		
		public static String urlAddPara(String url,String para){
			if(url.indexOf("Homepage.jsp")==-1) return url;
			if("".equals(url)) return "#";
			else {
				if(url.indexOf("?")!=-1) return url+"&"+para;
				else return url+"?"+para;
			}
		}


    /**
     * 删除字符串中的html格式
     *
     * @param input
     * @return
     */
    public static String HTMLtoTxt(String input) {
        if (input == null || input.trim().equals("")) {
            return "";
        }
        // 去掉所有html元素,
        String str = input.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "");
        return str;
    }
    
    /**
     * 判断文件是否为图片<br>
     * <br>
     * @param pInput 文件名<br>
     * @param pImgeFlag 判断具体文件类型<br>
     * @return 检查后的结果<br>
	*/
	public static boolean isPicture(String pInput, String pImgeFlag){
		// 文件名称为空的场合
		if("".equals(pInput)){
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1, pInput.length());
		// 声明图片后缀名数组
		String imgeArray [][] = {
			{"bmp", "0"}, {"dib", "1"}, {"gif", "2"},
			{"jfif", "3"}, {"jpe", "4"}, {"jpeg", "5"},
			{"jpg", "6"}, {"png", "7"} ,{"tif", "8"},
			{"tiff", "9"}, {"ico", "10"}
		};
		// 遍历名称数组
		for(int i=0; i<imgeArray.length;i++){
			// 判断单个类型文件的场合
			if(! "".equals(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase()) && imgeArray [i][1].equals(pImgeFlag)){
				return true;
			}
			//判断符合全部类型的场合
			if("".equals(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 遍历rourceList<String>，寻找是否有匹配regStr的项目，匹配忽略大小写。
	 * @param rourceList
	 * @param regStr
	 * @return
	 */
	public static int getListIndex(ArrayList rourceList, String regStr){
		int index = -1;
		try{
			regStr = Util.null2String(regStr);
			if(rourceList==null || rourceList.size()<1){
				index = -1;
			}else{
				for(int i=0; i<rourceList.size(); i++){
					String tmp = Util.null2String((String)rourceList.get(i)).trim();
					if(tmp.equalsIgnoreCase(regStr)){
						index = i;
						break;
					}
				}
			}
		}catch(Exception e){
			index = -1;
		}
		return index;
	}

	public static String stringReplace4DocDspExt(String resourceStr){
		String retStr = "";
		try{
			retStr=Util.StringReplace(resourceStr,"\\","\\\\");
			retStr=Util.StringReplace(retStr,"&lt;","<");
			retStr=Util.StringReplace(retStr,"&gt;",">");
			retStr=Util.StringReplace(retStr,"&quot;","\"");
			retStr=Util.StringReplace(retStr,""+'\n',"\n");
			retStr=Util.StringReplace(retStr,""+'\r',"\r");
			retStr=Util.StringReplace(retStr,"\"","\\\"");
			retStr=Util.StringReplace(retStr,"&#8226;","·");
		}catch(Exception e){
			retStr = resourceStr;
		}
		return retStr;
	}
	
	/**
     * 字符串转换   过滤掉字符串中部分的(")
     *
     * @return String   转换后的字符串
     */
     public static String toScreenForJs(String s) {
     	if (s == null){
     		s = null2String(s);
     	}
     	
     	String tempString=s;
     	try{
     	    tempString=StringReplace(tempString,"\\","\\\\"); 
     	    tempString=StringReplace(tempString,"\"","\\\""); 
     		return tempString;
     	}catch (Exception ee){
     		return tempString;
     	}
     }
     
	    public static boolean isExt(String fileExtendName) {
	    	
	    	boolean isExt=false;
	    	
	    	if(fileExtendName==null||fileExtendName.trim().equals("")){
	    		return isExt;
	    	}
	    	
	    	if(fileExtendName.equalsIgnoreCase("doc") ||fileExtendName.equalsIgnoreCase("xls") || fileExtendName.equalsIgnoreCase("ppt")|| fileExtendName.equalsIgnoreCase("docx") ||fileExtendName.equalsIgnoreCase("xlsx") || fileExtendName.equalsIgnoreCase("pptx") || fileExtendName.equalsIgnoreCase("wps") || fileExtendName.equalsIgnoreCase("et") ) {
	    		isExt=true;
	    	}
	    	
	    	return isExt;

	    }     
	 /**
     * 将英文单引号转换为中文单引号
     * @param s 转换前的字符
     * @return  转换后的字符
     */
    public static String convertDbInput(String s)
    {
         s = null2String(s);
         //s= s.replace("'", "\'");
         s= s.replace("'", "‘");

		return s;
    
    }
    

    /**
     * 将对象转换成字符串输出（只有包装类型可以正常输出，如果是类输出的是该类实例的地址）
     * @param obj
     * @return
     */
    public static String object2String(Object obj){
    	return obj==null?"":obj.toString();
    }
    
    
    
    public static boolean isExcuteFile(String fileName){
		boolean returnValue=false;
		int extNamePos=fileName.lastIndexOf(".");
		if(extNamePos!=-1){
			String extName=Util.null2String(fileName.substring(extNamePos+1));
			if(extName.equalsIgnoreCase("jsp")||extName.equalsIgnoreCase("php")){
				returnValue=true;
			}
		}
		return returnValue;
	}
    
    /**
		 * type 为  1  的时间格式为：    yyyy-MM-dd
		 * @param type
		 * @return
		 */
		public static String date(int type){
			String result="";
			SimpleDateFormat format=null;
			if(type==1)format=new SimpleDateFormat("yyyy-MM-dd");
			else if(type==2){format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); }
			else if(type==3){format=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss"); }
			Date now=new Date();
			result=format.format(now);
		    return result;	
		}
		
		public static String getRandomString(int length) {
		    StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		    StringBuffer sb = new StringBuffer();
		    Random r = new Random();
		    int range = buffer.length(); 
		    for (int i = 0; i < length; i ++) { 
		        sb.append(buffer.charAt(r.nextInt(range))); 
		    } 
		    return sb.toString(); 
		}
		
		/** zzl
		 * 解决字符串中，前面，中间，后面多余的逗号，返回左右无逗号的字符串
		 * 如：",,,,2,3" 输出 2,3
		 * 如：",,,,2,,3" 输出 2,3
		 * 如：",,,,2,3,,,,," 输出 2,3
		 * 如：",,2,,3,," 输出 2,3
		 * @param str
		 * @return
		 */
		public static String TrimComma(String str) {
				String returnstr="";
				String sz[]=str.split(",");
				for(int i=0;i<sz.length;i++){
					if(!"".equals(sz[i])){//主要用于排查字符串中的多个逗号的情况如：（3，，4，，，6，）
						returnstr+=sz[i]+",";
					}
				}
				int len = returnstr.length();
				int st = 0;
				int off = 0;
				char[] val = returnstr.toCharArray();
				while ((st < len) && (val[off + st] <= ',')) {
				st++;
				}
				while ((st < len) && (val[off + len - 1] <= ',')) {
				len--;
				}
				return ((st > 0) || (len < returnstr.length())) ? returnstr.substring(st, len): returnstr;
			}
	   public static String sendRedirect(String url,String isDialog){
		   return sendRedirect(url,"0","0",isDialog,"window");
	   }
	   public static String sendRedirect(String url,String id,String parentId,String isDialog,String target){
           StringBuilder html = new StringBuilder();
           html.append("<script type='text/javascript'>\n");
           if(!"".equals(isDialog)){
               html.append("parent.getParentWindow(window).parent.parent.refreshTree(").append(id).append(",").append(parentId).append(");\n");
           }else{
               html.append("parent.parent.refreshTreeMain(").append(id).append(",'").append(parentId).append("');\n");
           }
           html.append(target).append(".location.href='").append(url)
               .append("'\n</script>");
           return html.toString();
           
       }
       

	    


	 /**
	* 在JSP里，获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。
	* 但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了
	* 经过代理以后，由于在客户端和服务之间增加了中间层，因此服务器无法直接拿到客户端的IP，服务器端应用也无法直接通过转发请求的地址返回给客户端。
	* 但是在转发请求的HTTP头信息中，增加了X－FORWARDED－FOR信息。用以跟踪原有的客户端IP地址和原来客户端请求的服务器地址
	* 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值,
	* 取X-Forwarded-For中第一个非unknown的有效IP字符串。
	* 
	* */
	    public static String getIpAddr(HttpServletRequest request) {
	        String ip = request.getHeader("x-forwarded-for");
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	            ip = request.getHeader("Proxy-Client-IP");      
	        }      
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	            ip = request.getHeader("WL-Proxy-Client-IP");      
	        }      
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	            ip = request.getRemoteAddr();      
	        }   
	        if ((ip.indexOf(",") >= 0)){
	            ip = ip.substring(0 , ip.indexOf(","));
	        }
	        return ip;      
	    }
	    
	    
	    /**
	     * 截取字符串，超出的部分 省略号 替代
	     * @param s 字符串
	     * @param bytenum 截取的字节数
	     * @return
	     */
	    public static String getByteNumString(String s,int bytenum){
			if("".equals(s) || s == null) return "";
			int k=0;
			String str="";
			for (int i = 0; i <s.length(); i++)
			{	
				byte[] b=(s.charAt(i)+"").getBytes();
				k=k+b.length;
				if(k>bytenum)
				{
					break;
				}
				str=str+s.charAt(i);			
			}	
			
			if(s.getBytes().length>bytenum){
				str = str +"...";
			}
			
			return str;
		}
	    




	/**
	 * 检测文件格式是否合法
	 * @param filename  文件名，如rename.bat
	 * @param allowTypes   文件允许的格式，如[".csv",".xml"]
	 * @return
	 */
	public static boolean validateFileExt(String filename,String[] allowTypes){
		if(filename!=null && allowTypes!=null){
			for(int i=0;i<allowTypes.length;i++){
				if(filename.toLowerCase().endsWith(allowTypes[i].toLowerCase())){
					return true;
				}
			}
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 检测文件格式是否合法
	 * @param filename  文件名，如rename.bat
	 * @return
	 */
	public static boolean validateFileExt(String filename){
		String[] allowTypes  = new String[]{"exe","jsp","class","bat","jar"};
		if(filename!=null && allowTypes!=null){
			for(int i=0;i<allowTypes.length;i++){
				if(filename.toLowerCase().endsWith(allowTypes[i].toLowerCase())){
					return false;
				}
			}
			return true;
		}else{
			return true;
		}
	}
	
	public static String arrayUnion(String[] arr1, String[] arr2) {

        String result = "";

        int size1 = arr1.length;
        int size2 = arr2.length;

        if (size1 > size2) {
            for (int i = 0; i < size1; i++) {
                for (int k = 0; k < size2; k++) {
                    if (arr1[i] == arr2[k]) {
                        result += arr1[i] + ",";
                    }
                }
            }
        } else {
            for (int i = 0; i < size2; i++) {
                for (int k = 0; k < size1; k++) {
                    if (arr2[i] == arr1[k]) {
                        result += arr1[i] + ",";
                    }
                }
            }
        }

        result += "0";
        return result;
    }



}
