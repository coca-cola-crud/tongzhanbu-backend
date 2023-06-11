package com.shu.tongzhanbu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shu.tongzhanbu.admindb.repository.MemberRepository;
import com.shu.tongzhanbu.component.util.ResultBean;
import com.shu.tongzhanbu.component.util.constance.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ChartService {
    private final MemberRepository memberRepository;

    public ChartService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Object> objToList(Object obj) {
        List<Object> list = new ArrayList<Object>();
        if (obj instanceof ArrayList<?>) {
            for (Object o : (List<?>) obj) {
                list.add(o);
            }
            return list;
        }
        return null;
    }
    public ResultBean getPartypersonnum() {
      List dangpaiDTOs  = memberRepository.Partypersonnum();
        HashMap<String,Long> map = Constants.dangpaiValueMap;
        for(Object row:dangpaiDTOs){
            Object[] cells = (Object[]) row;
            map.put(Constants.dangpaiMap.get(cells[0]), Long.valueOf(String.valueOf(cells[1])));
        }

      return ResultBean.success(JSONObject.parse(JSON.toJSONString(map)));
    }

    public ResultBean getPartyPies(String dangpai) {
        JSONObject jsonObject = new JSONObject();
        List genderpiedata = memberRepository.PartyGender(dangpai);
        HashMap<String,Long> gendermap = new HashMap<>();
        gendermap.put("男",0L);
        gendermap.put("女",0L);
        for(Object row:genderpiedata){
            Object[] cells = (Object[]) row;
            gendermap.put((String) cells[0],Long.valueOf(String.valueOf(cells[1])));
        }
        jsonObject.put("genderpie",JSONObject.parse(JSON.toJSONString(gendermap)));

        List xueweipiedata = memberRepository.PartyXueWei(dangpai);
//        if(dangpai.equals("台盟")){
//            System.out.println(dangpai);
//            System.out.println(xueweipiedata);
//        }
        HashMap<String,Long> xueweimap = new HashMap<>(Constants.xueweiMapValue);
        Long othernum = 0L;
        for(Object row:xueweipiedata){
            Object[] cells = (Object[]) row;
            if (!Constants.xueweiMap.keySet().contains(cells[0])){
                othernum = othernum +Long.valueOf(String.valueOf(cells[1]));
            }else {
                xueweimap.put(Constants.xueweiMap.get(cells[0]),xueweimap.get(Constants.xueweiMap.get(cells[0]))+Long.valueOf(String.valueOf(cells[1])));
            }
        }
        xueweimap.put("其他",othernum);
        jsonObject.put("xueweipie",JSONObject.parse(JSON.toJSONString(xueweimap)));

        List zhiwujibiepiedata = memberRepository.PartyZj(dangpai);
        HashMap<String,Long> zhiwujibiemap = new HashMap<>(Constants.zwjbMapValue);
        for(Object row:zhiwujibiepiedata){
            Object[] cells = (Object[]) row;
            if(String.valueOf(cells[0]).contains("副处")){
                zhiwujibiemap.put("副处级",zhiwujibiemap.get("副处级")+Long.valueOf(String.valueOf(cells[1])));
            }
            if(String.valueOf(cells[0]).contains("正处")){
                zhiwujibiemap.put("正处级",zhiwujibiemap.get("正处级")+Long.valueOf(String.valueOf(cells[1])));
            }
//            if(String.valueOf(cells[0]).contains("正局")){
//                zhichengjibiemap.put("正局级",zhichengjibiemap.get("正局级")+Long.valueOf(String.valueOf(cells[1])));
//            }
            if(String.valueOf(cells[0]).contains("副局")){
                zhiwujibiemap.put("副局级",zhiwujibiemap.get("副局级")+Long.valueOf(String.valueOf(cells[1])));
            }
//            zhichengjibiemap.put(Constants.xueweiMap.get(cells[0]),Long.valueOf(String.valueOf(cells[1])));
        }
//        xueweimap.put("其他",othernum);
        jsonObject.put("zhiwujibiepie",JSONObject.parse(JSON.toJSONString(zhiwujibiemap)));


        List zcjbpiedata = memberRepository.PartyZcjb(dangpai);
        HashMap<String,Long> zcjbmap = new HashMap<>(Constants.zcjbMapValue);
        Long zcjbothernum = 0L;
        for(Object row:zcjbpiedata){
            Object[] cells = (Object[]) row;
            if(cells[0]!=null){
                if (!Constants.zcjbMapValue.keySet().contains(cells[0])&&!cells[0].equals("")){
                    zcjbothernum = zcjbothernum +Long.valueOf(String.valueOf(cells[1]));
                }else if(!cells[0].equals("")) {
                    zcjbmap.put((String) cells[0],Long.valueOf(String.valueOf(cells[1])));
                }
            }

        }
        zcjbmap.put("其他",zcjbothernum);
        jsonObject.put("zcjbpie",JSONObject.parse(JSON.toJSONString(zcjbmap)));
        return ResultBean.success(jsonObject);
    }

    public ResultBean getMinzu() {
        List minzu = memberRepository.Minzu();
        HashMap<String,Long> minzumap = new HashMap<>();
        for(Object row:minzu){
            Object[] cells = (Object[]) row;
            minzumap.put((String) cells[0],Long.valueOf(String.valueOf(cells[1])));
        }
        return ResultBean.success(JSONObject.parse(JSON.toJSONString(minzumap)));
    }

    public ResultBean getTongzhanparty() {
        List tongzhanparty = memberRepository.Tongzhanparty();
        HashMap<String,Long> tongzhanmap = Constants.tongzhanValueMap;
        for(Object row:tongzhanparty){
            Object[] cells = (Object[]) row;
            tongzhanmap.put(Constants.tongzhanMap.get(cells[0]),Long.valueOf(String.valueOf(cells[1])));
        }
        return ResultBean.success(JSONObject.parse(JSON.toJSONString(tongzhanmap)));
    }

    public ResultBean getDangpaizaili(){
        JSONObject jsonObject = new JSONObject();
        List zaizhi = memberRepository.dangpaizaizhi();
        HashMap<String,Long> zaizhimap =new HashMap<>(Constants.dangpaiValueMap);
        for(Object row:zaizhi){
            Object[] cells = (Object[]) row;
            zaizhimap.put(Constants.dangpaiMap.get(cells[0]), Long.valueOf(String.valueOf(cells[1])));
        }
        jsonObject.put("zaizhi",JSONObject.parse(JSON.toJSONString(zaizhimap)));
        List litui = memberRepository.dangpailitui();
        HashMap<String, Long> lituimap = new HashMap<String,Long>() {
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


        for(Object row:litui){
            Object[] cells = (Object[]) row;
            lituimap.put(Constants.dangpaiMap.get(cells[0]), Long.valueOf(String.valueOf(cells[1])));
        }
        jsonObject.put("litui",JSONObject.parse(JSON.toJSONString(lituimap)));
        return ResultBean.success(jsonObject);
    }

    public ResultBean getTongzhanPies(String tongzhan) {
        JSONObject jsonObject = new JSONObject();
        List genderpiedata = memberRepository.TongzhanGender(tongzhan);
        HashMap<String,Long> gendermap = new HashMap<>();
        gendermap.put("男",0L);
        gendermap.put("女",0L);
        for(Object row:genderpiedata){
            Object[] cells = (Object[]) row;
            gendermap.put((String) cells[0],Long.valueOf(String.valueOf(cells[1])));
        }
        jsonObject.put("genderpie",JSONObject.parse(JSON.toJSONString(gendermap)));

        List xueweipiedata = memberRepository.TongzhanXueWei(tongzhan);
//        if(tongzhan.equals("台盟")){
//            System.out.println(dangpai);
//            System.out.println(xueweipiedata);
//        }
        HashMap<String,Long> xueweimap = new HashMap<>(Constants.xueweiMapValue);

        Long othernum = 0L;
        for(Object row:xueweipiedata){
            Object[] cells = (Object[]) row;
            if (!Constants.xueweiMap.keySet().contains(cells[0])){
                othernum = othernum +Long.valueOf(String.valueOf(cells[1]));
            }else {
//                System.out.println(cells[0]);

                xueweimap.put(Constants.xueweiMap.get(cells[0]),xueweimap.get(Constants.xueweiMap.get(cells[0]))+Long.valueOf(String.valueOf(cells[1])));
            }
//            System.out.println(cells[0]);

        }
        xueweimap.put("其他",othernum);
        jsonObject.put("xueweipie",JSONObject.parse(JSON.toJSONString(xueweimap)));

        List zhiwujibiepiedata = memberRepository.TongzhanZj(tongzhan);
        HashMap<String,Long> zhiwujibiemap = new HashMap<>(Constants.zwjbMapValue);

        for(Object row:zhiwujibiepiedata){
            Object[] cells = (Object[]) row;
            if(String.valueOf(cells[0]).contains("副处")){
                zhiwujibiemap.put("副处级",zhiwujibiemap.get("副处级")+Long.valueOf(String.valueOf(cells[1])));
            }
            if(String.valueOf(cells[0]).contains("正处")){
                zhiwujibiemap.put("正处级",zhiwujibiemap.get("正处级")+Long.valueOf(String.valueOf(cells[1])));
            }
            if(String.valueOf(cells[0]).contains("正局")){
                zhiwujibiemap.put("正局级",zhiwujibiemap.get("正局级")+Long.valueOf(String.valueOf(cells[1])));
            }
            if(String.valueOf(cells[0]).contains("副局")){
                zhiwujibiemap.put("副局级",zhiwujibiemap.get("副局级")+Long.valueOf(String.valueOf(cells[1])));
            }

//            zhichengjibiemap.put(Constants.xueweiMap.get(cells[0]),Long.valueOf(String.valueOf(cells[1])));
        }
        xueweimap.put("其他",othernum);
        jsonObject.put("zhiwujibiepie",JSONObject.parse(JSON.toJSONString(zhiwujibiemap)));


        List zcjbpiedata = memberRepository.TongzhanZcjb(tongzhan);
        HashMap<String,Long> zcjbmap = new HashMap<>(Constants.zcjbMapValue);
        Long zcjbothernum = 0L;
        for(Object row:zcjbpiedata){
            Object[] cells = (Object[]) row;
            if(cells[0]!=null){
                if (!Constants.zcjbMapValue.keySet().contains(cells[0])&&!cells[0].equals("")){
                    zcjbothernum = zcjbothernum +Long.valueOf(String.valueOf(cells[1]));
                }else if(!cells[0].equals("")) {
                    zcjbmap.put((String) cells[0],Long.valueOf(String.valueOf(cells[1])));
                }
            }

        }
        zcjbmap.put("其他",zcjbothernum);
        jsonObject.put("zcjbpie",JSONObject.parse(JSON.toJSONString(zcjbmap)));

        return ResultBean.success(jsonObject);
    }
}
