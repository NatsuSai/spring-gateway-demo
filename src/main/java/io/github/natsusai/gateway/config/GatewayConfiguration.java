package io.github.natsusai.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

/**
 * @author liufuhong
 * @since 2020-04-16 11:03
 */

public class GatewayConfiguration {

  private final List<ViewResolver> viewResolvers;
  private final ServerCodecConfigurer serverCodecConfigurer;

  public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
      ServerCodecConfigurer serverCodecConfigurer) {
    this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
    this.serverCodecConfigurer = serverCodecConfigurer;
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
    // Register the block exception handler for Spring Cloud Gateway.
    return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
  }

  @Bean
  @Order(-1)
  public GlobalFilter sentinelGatewayFilter() {
    return new SentinelGatewayFilter();
  }

}
