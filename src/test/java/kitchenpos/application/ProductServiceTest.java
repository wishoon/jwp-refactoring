package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

    @Nested
    class 상품_생성 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final ProductCreateRequest product = new ProductCreateRequest("치킨", 1000);

            // when
            final ProductResponse extract = productService.create(product);

            // then
            assertThat(extract).isNotNull();
        }
    }

    @Nested
    class 상품_리스트_조회 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final ProductCreateRequest product = new ProductCreateRequest("치킨", 1000);
            productService.create(product);

            // when
            final List<ProductResponse> extract = productService.list();

            // then
            assertThat(extract).hasSize(1);
        }
    }
}