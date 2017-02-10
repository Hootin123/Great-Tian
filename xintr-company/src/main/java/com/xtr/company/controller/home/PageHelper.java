package com.xtr.company.controller.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageHelper {


	/**
	 * 分页页码
	 * @param totalcount 记录总数
	 * @param num 每页记录数
	 * @param index 当前页码
	 * @param paramstr 参数传&隔开，如：a=1&b=2
	 * @param instate 当第二页码与第一页不连续时，中间的替换字符串，空为不需要
	 * @return
	 */
	public static Map<String,Object> pages(int totalcount,int num,int index,String paramstr,String instate){
		
		int half=3;
		
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("page", "");
		
		if(totalcount<1){
			result.put("code", "1");
			result.put("msg", "totalcount错误");
			return result;
		}
		
		if(num<1){
			result.put("code", "2");
			result.put("msg", "num错误");
			return result;
		}
		
		if(index<1)
			index=1;
		
		int total=totalcount/num;
		if(totalcount%num!=0)
			total+=1;
		
		if(index>total)
			index=total;
		
		int leftm=((index-half)-1)*-1;
		if(leftm<0)
			leftm=0;
		
		int rightm=(index+half)-total;
		if(rightm<0)
			rightm=0;
		
		int left=index-half;
		if(left<1)
			left=1;
		
		int right=index+half;
		if(right>total)
			right=total;
		
		List<Integer> plist=new ArrayList<Integer>();
		for(int i=left;i<=right;i++){
			plist.add(i);
		}
		
		int f=plist.get(0);
		int l=plist.get(plist.size()-1);
		
		int lend=f-rightm;
		if(lend<1)
			lend=1;
		
		
		for(int i=f-1;i>=lend;i--){
			plist.add(0, i);
		}
		
		
		int rend=l+leftm;
		if(rend>total)
			rend=total;
		
		
		for(int i=l+1;i<=rend;i++){
			plist.add(i);
		}
		
		
		if(instate.length()>0){
			
			f=plist.get(0);
			l=plist.get(plist.size()-1);
			if(f>1)
				plist.add(0,1);
			
			if(l<total)
				plist.add(total);
		
		}
		
		
		List<Map<String,Object>> pagelist=new ArrayList<Map<String,Object>>();
		Map<String,Object> pg=null;
		for(int i=0;i<plist.size();i++){
			
			if(instate.length()>0 && i==1){
				if(plist.get(i)>2){
					pg=new HashMap<String,Object>();
					pg.put("value", "");
					pg.put("page", instate);
					pg.put("btn", "0");
					pg.put("idx", "0");
					
					pagelist.add(pg);
				}
			}
			
			pg=new HashMap<String,Object>();
			String vl=paramstr;
			if(vl.length()>0)
				vl+="&";
			
			vl+="page="+plist.get(i);
			pg.put("value", vl);
			pg.put("page", plist.get(i)+"");
			pg.put("btn", "1");
			pg.put("idx", "0");
			if(plist.get(i)==index)
				pg.put("idx", "1");
			
			pagelist.add(pg);
			
			if(instate.length()>0 && i==plist.size()-2){
				if(plist.get(i)<total-1){
					pg=new HashMap<String,Object>();
					pg.put("value", "");
					pg.put("page", instate);
					pg.put("btn", "0");
					pg.put("idx", "0");
					
					pagelist.add(pg);
				}
			}
		}
		
		int pre=index-1;
		if(pre<1)
			pre=1;
		
		int next=index+1;
		if(next>total)
			next=total;
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("pagelist", pagelist);
		map.put("pagenum", num+"");
		map.put("index", index+"");
		map.put("pre", pre+"");
		map.put("next", next+"");
		map.put("first", "1");
		map.put("last", total+"");
		
		
		
		result.put("code", "0");
		result.put("msg", "");
		result.put("page", map);	
		
		return result;
	}

}
