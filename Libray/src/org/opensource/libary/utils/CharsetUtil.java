package org.opensource.libary.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CharsetUtil {
	private static String[] charsets = new String[]{"utf-8" , "gbk"};  
    
    public static String getCharset(String word) {  
        for(String charset : charsets) {  
            try {  
                String decodeWord = URLDecoder.decode(word, charset);  
                String encodeWord= URLEncoder.encode(decodeWord, charset);  
                if(word.equals(encodeWord))  
                    return charset;  
            } catch (UnsupportedEncodingException e) {  
                continue;  
            }  
        }  
        return "utf-8";  
    }  
}
