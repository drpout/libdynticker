# libdynticker

Java library to retrieve trading pairs and last value from cryptocurrency and bullion exchanges.

The main goal of libdynticker is to get the traded pairs dynamically. That way it can cope with the highly mutable altcoin exchanges where new pairs are added and removed each week. The result of this is that once a new exchange is added all trading pairs come along immediately.

## Supported exchanges

### Bitcoin and altcoins to FIAT
* [ANX](https://anxpro.com)
* [Bitcoin.de](https://www.bitcoin.de)
* [Bitfinex](https://www.bitfinex.com)
* [Bitstamp](https://www.bitstamp.net)
* [BTC China](https://btcchina.com)
* [BTC Markets](https://btcmarkets.net)
* [BTC-E](https://btc-e.com)
* [Bter](https://bter.com)
* [CampBX](http://campbx.com)
* [CaVirtEx](https://www.cavirtex.com)
* [CEX.IO](http://cex.io)
* [CHBTC](https://www.chbtc.com)
* [Huobi](https://www.huobi.com)
* [itBit](https://www.itbit.com)
* [Kraken](https://www.kraken.com)
* [LakeBTC](http://lakebtc.com)
* [LocalBitcoins](https://localbitcoins.com)
* [OKCoin](https://www.okcoin.cn)
* [OKCoin International](https://www.okcoin.com)
* [Peatio](https://peatio.com)

### Altcoins
* [AllCoin](https://www.allcoin.com)
* [Bittrex](https://bittrex.com)
* [BlueTrade](https://bleutrade.com)
* [BTC38](http://www.btc38.com)
* [Cryptsy](https://cryptsy.com)
* [Coin-Swap](https://coin-swap.net)
* [HitBTC](https://hitbtc.com)
* [Mintpal](https://mintpal.com)
* [Poloniex](https://www.poloniex.com)
* [VoS](https://www.vaultofsatoshi.com)

### Bullion
* [BullionVault](https://www.bullionvault.com)
* [Melotic](https://www.melotic.com)

### Futures and stocks
* [796 Exchange](https://796.com)

## Building
 `mvn package` to create a jar.

## Versioning
libdynticker follows [Semantic Versioning](http://semver.org) with the API being the public methods and attributes provided by the [Exchange](/src/main/java/mobi/boilr/libdynticker/core/Exchange.java) and [Pair](/src/main/java/mobi/boilr/libdynticker/core/Pair.java) classes.

## License
All libdynticker code is licensed under [GNU LGPL v3](/LICENSE) or later. Copyright belongs to the [contributors listed on GitHub](https://github.com/andrefbsantos/libdynticker/graphs/contributors).