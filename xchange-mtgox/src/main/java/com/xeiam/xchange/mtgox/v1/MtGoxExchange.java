/**
 * Copyright (C) 2012 - 2013 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.mtgox.v1;

import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.mtgox.MtGoxExchangeServiceConfiguration;
import com.xeiam.xchange.mtgox.v1.service.account.MtGoxPollingAccountService;
import com.xeiam.xchange.mtgox.v1.service.marketdata.polling.MtGoxPollingMarketDataService;
import com.xeiam.xchange.mtgox.v1.service.marketdata.streaming.socketio.MtGoxStreamingMarketDataService;
import com.xeiam.xchange.mtgox.v1.service.trade.polling.MtGoxPollingTradeService;
import com.xeiam.xchange.service.ExchangeServiceConfiguration;
import com.xeiam.xchange.service.marketdata.streaming.StreamingMarketDataService;

import java.io.IOException;

/**
 * <p>
 * Exchange implementation to provide the following to applications:
 * </p>
 * <ul>
 * <li>A wrapper for the MtGox exchange API</li>
 * </ul>
 */
public class MtGoxExchange extends BaseExchange implements Exchange {

  ExchangeSpecification specification;

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {

    if (exchangeSpecification == null) {
      exchangeSpecification = getDefaultExchangeSpecification();
    }
    this.specification = exchangeSpecification;

    // Configure the basic services if configuration does not apply
    this.pollingMarketDataService = new MtGoxPollingMarketDataService(exchangeSpecification);
    this.pollingTradeService = new MtGoxPollingTradeService(exchangeSpecification);
    this.pollingAccountService = new MtGoxPollingAccountService(exchangeSpecification);

  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setUri("https://mtgox.com");
    exchangeSpecification.setHost("mtgox.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("MtGox");
    exchangeSpecification.setExchangeDescription("MtGox is a Bitcoin exchange registered in Japan.");

    return exchangeSpecification;
  }

  @Override
  public StreamingMarketDataService getStreamingMarketDataService(ExchangeServiceConfiguration configuration) {

    if (configuration instanceof MtGoxExchangeServiceConfiguration) {
      try {
        return new MtGoxStreamingMarketDataService(specification, (MtGoxExchangeServiceConfiguration) configuration);
      } catch (IOException e) {
        throw new ExchangeException("Streaming market data service failed", e);
      }
    }

    throw new IllegalArgumentException("MtGox only supports the MtGoxExchangeServiceConfiguration");
  }
}
