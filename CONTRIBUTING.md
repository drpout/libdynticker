# Contributing to libdynticker

## Adding new exchanges

1. Add a new class to [src/main/java/mobi/boilr/libdynticker/exchanges](/src/main/java/mobi/boilr/libdynticker/exchanges) extending [mobi.boilr.libdynticker.core.Exchange](/src/main/java/mobi/boilr/libdynticker/core/Exchange.java). Implement the constructor, getPairsFromAPI, getTicker and parseTicker methods. There are many implemented exchanges which you can use as examples.

	Do's and Don'ts:
	* If the exchange's API gives status/error information use it inside getPairsFromAPI and getTicker to throw descriptive IOExceptions. Look at [VircurexExchange](/src/main/java/mobi/boilr/libdynticker/exchanges/VircurexExchange.java) for an example.
	* If, and only if, the API has no method to retrieve a list of traded pairs, hardcode them in an unmodifiable list. On getTicker method you can make sure a pair is valid by checking whether it is in the unmodifiable list. There is an example in [ChbtcExchange](/src/main/java/mobi/boilr/libdynticker/exchanges/ChbtcExchange.java).
	* Some exchanges return strange HTTP headers which make the HttpURLConnection throw a FileNotFoundException. To solve it create an explicit HttpURLConnection and add the `Accept: */*` request property. Example at [MeloticExchange](/src/main/java/mobi/boilr/libdynticker/exchanges/MeloticExchange.java).
	* Some poorly designed APIs use a numeric ID to represent a pair (instead of the usual string concatenation of coin plus exchange). To handle this you can store the ID in the market attribute of the [Pair](/src/main/java/mobi/boilr/libdynticker/core/Pair.java) class. Check [CCEDKExchange](/src/main/java/mobi/boilr/libdynticker/exchanges/CCEDKExchange.java) for an example.
	* If the exchange uses a self-signed HTTPS certificate you can lead Java into accepting it by using the dirty hack present at [CoinswapExchange](https://github.com/andrefbsantos/libdynticker/blob/e1b7dd16542a10be262d32887daf54608a723c37/src/main/java/mobi/boilr/libdynticker/exchanges/CoinswapExchange.java).

2. Add a new test class to [src/test/java/mobi/boilr/libdynticker/exchanges](/src/test/java/mobi/boilr/libdynticker/exchanges) extending [mobi.boilr.libdynticker.core.ExchangeTest](/src/test/java/mobi/boilr/libdynticker/core/ExchangeTest.java). Implement the setUp, tearDown, testGetPairs and testParseTicker methods. You can use the already implemented tests as templates.

	Do's and Don'ts:
	* Remember to save an example JSON of the exchange's ticker in [src/test/json](/src/test/json) to use in testParseTicker.
	* If the exchange lists pairs that turn out to have an empty ticker testGetLastValueWithPairsFromGetPairs will fail. You can circumvent this by overriding the handleException method. Example at [CCEDKExchangeTest](/src/test/java/mobi/boilr/libdynticker/exchanges/CCEDKExchangeTest.java).
