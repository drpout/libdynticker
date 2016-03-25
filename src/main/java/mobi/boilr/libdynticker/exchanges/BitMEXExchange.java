package mobi.boilr.libdynticker.exchanges;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BitMEXExchange extends Exchange {
  private static final String API_URL = "https://www.bitmex.com/api/v1";

  public BitMEXExchange(long expiredPeriod) {
    super("BitMEX", expiredPeriod);
  }

  @Override
  protected List<Pair> getPairsFromAPI() throws IOException {
      String url = API_URL + "/instrument/active?columns=underlying,quoteCurrency";

      JsonNode node = readJsonFromUrl(url);
      List<Pair> pairs = new ArrayList<Pair>();
      Iterator<JsonNode> instruments = node.getElements();
      while(instruments.hasNext()) {
          JsonNode instrument = instruments.next();
          String underlying = instrument.get("underlying").asText();
          String quoteCurrency = instrument.get("quoteCurrency").asText();
          String symbol = instrument.get("symbol").asText();
          pairs.add(new Pair(underlying, quoteCurrency, symbol));
      }

      return pairs;
  }

  @Override
  protected String getTicker(Pair pair) throws IOException {
      List<Pair> pairs = this.getPairs();
      if(!pairs.contains(pair)) {
          throw new IOException("Invalid pair: " + pair);
      }

      String url = API_URL + "/instrument?symbol=" + pair.getMarket() + "&columns=lastPrice";

      JsonNode node = readJsonFromUrl(url);
      System.out.println(node.get(0).get("lastPrice").toString());
      if(node.get(0) == null) {
          throw new IOException("Invalid pair: " + pair);
      }
      return parseTicker(node, pair);
  }

  @Override
  public String parseTicker(JsonNode node, Pair pair) {
    return node.get(0).get("lastPrice").toString();
  }
}
