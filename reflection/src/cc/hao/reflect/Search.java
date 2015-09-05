package cc.hao.reflect;

import java.util.ArrayList;
import java.util.List;

public class Search implements Action{

	@Override
	public List<String> act(List<String> params) {
		// TODO Auto-generated method stub
		List<String> result = new ArrayList<>();
		result.add("this is search");
		return result;
	}

}
