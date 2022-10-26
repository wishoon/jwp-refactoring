package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TableServiceTest {

    @Nested
    class 테이블_생성 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final OrderTable orderTable = new OrderTable(null, 0, true);

            // when
            final OrderTable extract = tableService.create(orderTable);

            // then
            assertThat(extract).isNotNull();
        }
    }

    @Nested
    class 테이블_목록 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final OrderTable orderTable = new OrderTable(null, 0, true);
            tableService.create(orderTable);

            // when
            final List<OrderTable> extract = tableService.list();

            // then
            assertThat(extract).hasSize(1);
        }
    }

    @Nested
    class 테이블_상태_변경 extends IntegrationTest {
        @ParameterizedTest(name = "[{index}] 주문의 상태가 {0} 인 경")
        @CsvSource(value = {"COOKING", "MEAL"})
        void 요청에서_테이블이_비어있는_상태로_변경할때_주문의_상태가_완료상태가_아니면_예외가_발생한다(final OrderStatus orderStatus) {
            // given
            final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            final Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            final Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
            final Menu saveMenu = menuService.create(createMenu);
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));
            final Order order = orderService.create(new Order(orderTable.getId(), orderStatus.name(),
                LocalDateTime.now(), List.of(new OrderLineItem(saveMenu.getId(), 1))));

            // when & then
            assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), new OrderTable(orderTable.getId(), 2, false)))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_테이블의_손님의_수를_변경할_수_있다() {
            // given
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

            // when
            final OrderTable extract = tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(null, 3, false));

            // then
            assertThat(extract.getNumberOfGuests()).isEqualTo(3);
        }

        @Test
        void 요청에서_테이블의_손님의_수를_0원_미만으로_변경하면_예외가_발생한다() {
            // given
            final OrderTable orderTable = tableService.create(new OrderTable(null, 2, false));

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(null, -1, false)))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청에서_비어있는_테이블에_손님의_수를_변경할_경우_예외가_발생한다() {
            // given
            final OrderTable extract = tableService.create(new OrderTable(null, 0, true));

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(extract.getId(), new OrderTable(null, 1, true)))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}