package com.github.andrefbsantos.libdynticker.hitbtc;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

/**
 * 
 */

/**
 * @author andre
 *
 */
public class HitbtcExchange extends Exchange {

	/* (non-Javadoc)
	 * @see com.github.andrefbsantos.libdynticker.core.Exchange#getPairs()
	 */
	@Override
	public List<Pair> getPairs() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();

		Iterator<JsonNode> elements = (new ObjectMapper()).readTree(new URL("https://api.hitbtc.com/api/1/public/symbols")).get("symbols").getElements();


		while (elements.hasNext()) {
			JsonNode element = elements.next();
			String textValue = element.get("symbol").getTextValue();

			if(textValue.length()==6){
				String coin = textValue.substring(0, 3);
				String exchange = textValue.substring(3, 6);
				System.out.println(coin +"/"+exchange);

				pairs.add(new Pair(coin, exchange));
			}else if(textValue.startsWith("DOGE")){
				String coin = textValue.substring(0, 4);
				String exchange = textValue.substring(4, 7);
				System.out.println(coin +"/"+exchange);
				pairs.add(new Pair(coin, exchange));
			}
		}		
		return pairs;
	}

	/* (non-Javadoc)
	 * @see com.github.andrefbsantos.libdynticker.core.Exchange#getTickerURL(com.github.andrefbsantos.libdynticker.core.Pair)
	 */
	protected String getTickerURL(Pair pair) {
		return "https://api.hitbtc.com/api/1/public/"+pair.getCoin()+pair.getExchange()+"/ticker";
	}

	/* (non-Javadoc)
	 * @see com.github.andrefbsantos.libdynticker.core.Exchange#parseJSON(org.codehaus.jackson.JsonNode, com.github.andrefbsantos.libdynticker.core.Pair)
	 */
	protected String parseJSON(JsonNode node, Pair pair) {
		return node.get("last").getTextValue();
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON(new ObjectMapper().readTree(new URL(this.getTickerURL(pair))), pair);
	}

}
