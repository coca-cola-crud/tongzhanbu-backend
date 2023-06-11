package com.shu.tongzhanbu.component.util.constance;

import io.swagger.models.auth.In;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author cxy
 * @Date: 2020/12/6 14:36
 * @Description: 常量类
 */
public final class Constants {
    public static final HashMap<String, String> dangpaiMap = new HashMap<String,String>() {
        {
//            put("中共党员", "中共");
            put("九三学社", "九三学社");
            put("农工党", "农工党");
            put("无党派人士", "无党派人士");
            put("民建", "民建");
            put("民进", "民进");
            put("民革", "民革");
            put("民盟", "民盟");
            put("致公党", "致公党");
            put("台盟", "台盟");
        }
    };
    public static final HashMap<String, Long> dangpaiValueMap = new HashMap<String,Long>() {
        {
//            put("中共", 0L);
            put("九三学社", 0L);
            put("农工党", 0L);
            put("无党派人士", 0L);
            put("民建", 0L);
            put("民进", 0L);
            put("民革", 0L);
            put("民盟",0L);
            put("致公党", 0L);
            put("台盟", 0L);
        }
    };
    public static final HashMap<String, String> tongzhanMap = new HashMap<String,String>() {
        {
            put("上海大学党外知识分子联谊会", "上海大学党外知识分子联谊会");
            put("上海大学少数民族联合会","上海大学少数民族联合会");
            put("上海大学归国华侨联合会","上海大学归国华侨联合会");
            put("上海市欧美同学会上海大学分会","上海市欧美同学会上海大学分会");
        }
    };

    public static final HashMap<String, Long> tongzhanValueMap = new HashMap<String,Long>() {
        {
            put("上海大学党外知识分子联谊会",0L);
            put("上海大学少数民族联合会",0L);
            put("上海大学归国华侨联合会",0L);
            put("上海市欧美同学会上海大学分会",0L);

        }
    };


    public static final HashMap<String, Long> tongzhaninputMap = new HashMap<String,Long>() {
        {
            put("上海大学党外知识分子联谊会",1L);
            put("上海大学少数民族联合会",2L);
            put("上海大学归国华侨联合会",3L);
            put("上海市欧美同学会上海大学分会",4L);

        }
    };

    public static final HashMap<String, String> xueweiMap = new HashMap<String,String>() {
        {
            put("博士研究生", "博士研究生");
            put("硕士研究生", "硕士研究生");
            put("大学本科", "大学本科");
            put("大学毕业", "大学本科");
            put("博士研究生毕业", "博士研究生");
            put("研究生毕业", "硕士研究生");
            put("硕士研究生毕业", "硕士研究生");
        }
    };
    public static final HashMap<String, Long> xueweiMapValue = new HashMap<String,Long>() {
        {
            put("博士研究生", 0L);
            put("硕士研究生", 0L);
            put("大学本科", 0L);

        }
    };
    public static final HashMap<String, Long> zwjbMapValue = new HashMap<String,Long>() {
        {
            put("正处级", 0L);
            put("副处级", 0L);
            put("副局级", 0L);

        }
    };

    public static final HashMap<String, Long> zcjbMapValue = new HashMap<String,Long>() {
        {
            put("正高级", 0L);
            put("副高级", 0L);
            put("中级", 0L);

        }
    };

    public static final String AGE_30 = "30岁及以下";
    public static final String AGE_30_40="31-40岁";
    public static final String AGE_40_50="41-50岁";
    public static final String AGE_50_60="51-60岁";
    public static final String AGE_60="60岁及以上";


    //人事处数据对应
    public static final HashMap<String, String> rscValuemap = new HashMap<String,String>() {
        {
//            put("在编状态", "yuangongzu");
            put("职称", "jishuzhicheng");
            put("职称级别", "zhichengjibie");
            put("部门", "dep1");
            put("子部门","dep2");
            put("职务","jobInfo");
//            put("学位","xuewei");
            put("性别","gender");
            put("民族","minzu");
//            put("职级","zhiwulevel");
            put("状态","yuangongzizu");

        }
    };






}