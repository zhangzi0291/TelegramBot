package com.bot.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 引用https://blog.csdn.net/hantiannan/article/details/6756347
 */
public class CsvParser {

    private BufferedReader bufferedreader = null;
    private List<String> list = new ArrayList();

    public CsvParser() {
    }

    public CsvParser(Path path, Charset charset) throws Exception {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(path.toString()), charset.name());
        bufferedreader = new BufferedReader(isr);
        String stemp;
        while ((stemp = readLine()) != null) {
            list.add(stemp);
        }
    }

    public CsvParser(InputStream inStream) throws Exception {
        InputStreamReader isr = new InputStreamReader(inStream, "GBK");
        bufferedreader = new BufferedReader(isr);
        String stemp;
        while ((stemp = readLine()) != null) {
            list.add(stemp);
        }
    }

    /**
     * 过滤回车和英文逗号
     *
     * @return
     * @throws Exception
     */
    public String readLine() throws Exception {
        StringBuilder readLine = new StringBuilder();
        boolean bReadNext = true;
        while (bReadNext) {
            //换行符替换
            if (readLine.length() > 0) {
                readLine.append("\r\n");
            }
            // 一行
            String strReadLine = bufferedreader.readLine();
            // readLine is Null
            if (strReadLine == null) {
                return readLine.length() <= 0 ? null : readLine.toString();
            }
            readLine.append(strReadLine);
            // 如果双引号是奇数的时候继续读取（含有换行时会添加双引号，自带的双引号会转为2个双引号）
            if (countChar(readLine.toString(), '"', 0) % 2 == 1) {
                bReadNext = true;
            } else {
                bReadNext = false;
            }
        }
        return readLine.toString();
    }

    /**
     *计算指定文字的个数。
     *
     * @param str 文字列
     * @param c 文字
     * @param start  开始位置
     * @return 个数
     */
    private int countChar(String str, char c, int start) {
        int i = 0;
        int index = str.indexOf(c, start);
        return index == -1 ? i : countChar(str, c, index + 1) + 1;
    }

    public List<String> getList() throws IOException {
        return list;
    }/**
     * csv文件的行数
     *
     * @return
     */
    public int getRowNum() {
        return list.size();
    }

    /**
     * 取得指定行的值
     *
     * @param index
     * @return
     */
    public String getRow(int index) {
        if (this.list.size() != 0) {
            return (String) list.get(index);
        } else {
            return null;
        }
    }
    /**
     * 把CSV文件的一行转换成字符串数组。不指定数组长度。
     */
    public List<String> fromCSVLinetoArray(String source) {
        if (source == null || source.length() == 0) {
            return Collections.emptyList();
        }
        int currentPosition = 0;
        int maxPosition = source.length();
        int nextComma = 0;
        List<String> rtnArray = new ArrayList();
        while (currentPosition < maxPosition) {
            nextComma = nextComma(source, currentPosition);
            rtnArray.add(nextToken(source, currentPosition, nextComma));
            currentPosition = nextComma + 1;
            if (currentPosition == maxPosition) {
                rtnArray.add("");
            }
        }
        return rtnArray;
    }

    /**
     * 查询下一个逗号的位置。
     *
     * @param source 文字列
     * @param st  检索开始位置
     * @return 下一个逗号的位置。
     */
    private int nextComma(String source, int st) {
        int maxPosition = source.length();
        boolean inquote = false;
        while (st < maxPosition) {
            char ch = source.charAt(st);
            if (!inquote && ch == ',') {
                break;
            } else if ('"' == ch) {
                inquote = !inquote;
            }
            st++;
        }
        return st;
    }

    /**
     * 取得下一个字符串
     */
    private static String nextToken(String source, int st, int nextComma) {
        StringBuilder strb = new StringBuilder();
        int next = st;
        while (next < nextComma) {
            char ch = source.charAt(next++);
            if (ch == '"') {
                if ((st + 1 < next && next < nextComma) && (source.charAt(next) == '"')) {
                    strb.append(ch);
                    next++;
                }
            } else {
                strb.append(ch);
            }
        }
        return strb.toString();
    }
    /**
     * 获取所有行(不含表头)
     *
     * @return 行列表，且每行是一个列表
     */
    public List<List<String>> getRowsWithNoHeader() {
        List<List<String>> result = new ArrayList<List<String>>();
        for (int i = 1; i < getRowNum(); i++) {
            String row = getRow(i);
            List<String> oneRow = this.fromCSVLinetoArray(row);
            if (oneRow!=null && oneRow.size()>0) {
                result.add(oneRow);
            }
        }
        return result;
    }



}
