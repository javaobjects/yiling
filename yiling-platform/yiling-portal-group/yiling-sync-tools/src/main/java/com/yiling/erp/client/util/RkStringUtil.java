package com.yiling.erp.client.util;

import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.util.StrUtil;

/**
 * @author jian.mei
 * @CreateDate: 2019-11-18
 *
 */
public class RkStringUtil {

	private static final char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private static final String regValidatorIp = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
	private static final String regValidator24Hour = "^([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";
	private static final String regValidatorLetterAndNumber = "^[0-9A-Za-z]+$";
	private static final String regValidatorMobile = "^[1]{1}[3-9]{1}[0-9]{9}$";

	/**
	 * 获得0-9的随机数
	 * 
	 * @param length
	 * @return String
	 */
	public static String getRandomNumber(int length) {
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < length; i++) {
			buffer.append(random.nextInt(10));
		}
		return buffer.toString();
	}

	/**
	 * 获得0-9,a-z,A-Z范围的随机数
	 * 
	 * @param length 随机数长度
	 * @return String
	 */

	public static String getRandomChar(int length) {

		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(chr[random.nextInt(36)]);
		}
		return buffer.toString();
	}

	/**
	 * 检查字符串是否为空
	 * <p>
	 * 为null或者长度为0视为空字符串
	 * 
	 * @param value     要检查的字符串
	 * @param trim      是否去掉头尾的特定字符
	 * @param trimChars 要去掉的特定字符
	 * @return
	 */
	public static boolean isEmpty(String value, boolean trim, char... trimChars) {
		if (trim) {
            return value == null || trim(value, trimChars).length() <= 0;
        }
		return value == null || value.length() <= 0;
	}

	/**
	 * 检查字符串是否为空
	 * <p>
	 * 为null或者长度为0视为空字符串
	 * 
	 * @param value 要检查的字符串
	 * @param trim  是否去掉头尾的空格
	 * @return
	 */
	public static boolean isEmpty(String value, boolean trim) {
		return isEmpty(value, trim, ' ');
	}

	/**
	 * 检查字符串是否为空
	 * <p>
	 * 为null或者长度为0视为空字符串
	 * 
	 * @param value 要检查的字符串
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return isEmpty(value, false);
	}

	/**
	 * 如果为null，转换为""
	 * 
	 * @param value
	 * @return
	 */
	public static String nullSafeString(String value) {
		return value == null ? "" : value;
	}

	/**
	 * 确保存入数据库的string值不会引起数据库报错。
	 * <p>
	 * 1. 数据库不允许为null，value为nul时返回""；<br />
	 * 2. 超过最大长度时截断字符串。
	 * 
	 * @param value     要存入数据库的字符串值。
	 * @param nullable  是否允许为null。
	 * @param maxLength 最大长度。
	 * @return
	 */
	public static String dbSafeString(String value, boolean nullable, int maxLength) {
		if (value == null) {
			if (nullable) {
                return null;
            }
			return nullSafeString(value);
		}
		if (value.length() > maxLength) {
            return value.substring(0, maxLength);
        }
		return value;
	}

	/**
	 * 去掉头尾空格字符
	 * 
	 * @param value 待处理的字符串
	 * @return
	 */
	public static String trim(String value) {
//		return trim(3, value, ' ');
        return StrUtil.trim(value);
	}

	/**
	 * 去除字符串头尾的特定字符
	 * 
	 * @param value 待处理的字符串
	 * @param chars 需要去掉的特定字符
	 * @return
	 */
	public static String trim(String value, char... chars) {
		return trim(3, value, chars);
	}

	/**
	 * 去除字符串头部的特定字符
	 * 
	 * @param value 待处理的字符串
	 * @param chars 需要去掉的特定字符
	 * @return
	 */
	public static String trimStart(String value, char... chars) {
		return trim(1, value, chars);
	}

	/**
	 * 去除字符串尾部的特定字符
	 * 
	 * @param value 待处理的字符串
	 * @param chars 需要去掉的特定字符
	 * @return
	 */
	public static String trimEnd(String value, char... chars) {
		return trim(2, value, chars);
	}

	/**
	 * 去掉字符串头尾特定字符
	 * 
	 * @param mode
	 *              <li>1: 去掉头部特定字符；
	 *              <li>2: 去掉尾部特定字符；
	 *              <li>3: 去掉头尾特定字符；
	 * @param value 待处理的字符串
	 * @param chars 需要去掉的特定字符
	 * @return
	 */
	private static String trim(int mode, String value, char... chars) {
		if (value == null || value.length() <= 0) {
            return value;
        }

		int startIndex = 0, endIndex = value.length(), index = 0;
		if (mode == 1 || mode == 3) {
			// trim头部
			while (index < endIndex) {
				if (contains(chars, value.charAt(index++))) {
					startIndex++;
					continue;
				}
				break;
			}
		}

		if (startIndex >= endIndex) {
            return "";
        }

		if (mode == 2 || mode == 3) {
			// trim尾部
			index = endIndex - 1;
			while (index >= 0) {
				if (contains(chars, value.charAt(index--))) {
					endIndex--;
					continue;
				}
				break;
			}
		}

		if (startIndex >= endIndex) {
            return "";
        }
		if (startIndex == 0 && endIndex == value.length() - 1) {
            return value;
        }

		return value.substring(startIndex, endIndex);
	}

	private static boolean contains(char[] chars, char chr) {
		if (chars == null || chars.length <= 0) {
            return false;
        }
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == chr) {
                return true;
            }
		}
		return false;
	}

	/**
	 * 判断是否是有效的IP地址。
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isIp(String value) {
		if (isEmpty(value))
			return false;
		return value.matches(regValidatorIp);
	}

	/**
	 * 判断是否是有效的24小时制的时间部分（hh:mm:ss）
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isTime(String value) {
		if (isEmpty(value))
			return false;
		return value.matches(regValidator24Hour);
	}

	/**
	 * 判断是否由字符和数字组成
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isLetterAndNumber(String value) {
		if (isEmpty(value))
			return false;
		return value.matches(regValidatorLetterAndNumber);
	}

	/**
	 * 判断字符串是否手机号
	 * 
	 * @param value
	 * @return
	 */
	public static Boolean isMobile(String value) {
		if (StringUtil.isEmpty(value))
			return false;
		return value.matches(regValidatorMobile);
	}

	/**
	 * 去除字符串前，后，中的空格
	 * 
	 * @param value
	 * @return
	 */
	public static String bankDir(String value) {
		String[] codes = { " " };
		for (int i = 0; i < codes.length; i++) {
			value = value.replace(codes[i], "");
		}
		return value;
	}

	/**
	 * Set集合转换成String
	 * 
	 * @param Set <String> args,String split_char 分隔符
	 * @return String
	 */
	public static String setToString(Set<String> args, String split_char) {
		if (args == null || args.isEmpty()) {
			return "";
		}
		String strs = "";
		for (String str : args) {
			strs += split_char + str;
		}
		if (strs.isEmpty()) {
			return "";
		}
		return strs.substring(split_char.length());
	}

	/**
	 * 验证字符串是否是邮箱格式
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmail(String email) {
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * 验证字符串是否包含中文
	 * 
	 * @param value
	 * @return true 包含
	 */
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}
	
	public static String hidenMobileNum(String mobile) {
		int length = mobile.length();
		if(length <= 7) {
			return mobile;
		}
		
		StringBuffer hiden = new StringBuffer();
		for(int i=7;i<length;i++) {
			hiden.append("*");
		}
    	return mobile.substring(0,3) + hiden.toString() + mobile.substring(length - 4);
    }


}
