package cc.hao.reflect;

import java.util.ArrayList;
import java.util.List;

public class Store implements Action{

	@Override
	public List<String> act(List<String> params) {
		List<String> result = new ArrayList<>();
		result.add("this is store");
		return result;
	}
}
