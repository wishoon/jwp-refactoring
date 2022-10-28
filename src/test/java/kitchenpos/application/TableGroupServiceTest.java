package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends IntegrationTest {

    @Nested
    class 테이블_그룹_생성 extends IntegrationTest {

        @Test
        void 요청으로_하나의_테이블로_묶을_수_있다() {
            // given
            final OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));
            final OrderTable orderTable2 = tableService.create(new OrderTable(null, 3, true));

            // when
            final TableGroup extract = tableGroupService.create(
                new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

            // then
            assertThat(extract).isNotNull();
        }

        @Test
        void 요청으로_하나의_테이블로_묶을_때_빈_테이블이_아닌_테이블이_있는경우_예외가_발생한다() {
            // given
            final OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));
            final OrderTable orderTable2 = tableService.create(new OrderTable(null, 3, false));

            // when & then
            assertThatThrownBy(
                () -> tableGroupService.create(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2))))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청으로_하나의_테이블로_묶을때_2개_미만일_경우_예외가_발생한다() {
            // given
            final OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));

            // when & then
            assertThatThrownBy(
                () -> tableGroupService.create(new TableGroup(LocalDateTime.now(), List.of(orderTable1))))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청으로_하나의_테이블로_묶을때_이미_단체로_지정된_테이블이_속한경우_예외가_발생한다() {
            // given
            final OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));
            final OrderTable orderTable2 = tableService.create(new OrderTable(null, 3, true));
            tableGroupService.create(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

            // when
            final OrderTable orderTable3 = tableService.create(new OrderTable(null, 3, true));
            assertThatThrownBy(
                () -> tableGroupService.create(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable3))))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_해제 extends IntegrationTest {

        @Test
        void 요청으로_지정된_그룹을_해제할_수_있다() {
            // given
            final OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));
            final OrderTable orderTable2 = tableService.create(new OrderTable(null, 3, true));
            final TableGroup tableGroup = tableGroupService.create(
                new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

            // when & then
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        }

        @Test
        void 요청으로_지정된_그룹을_해제할_때_주문의_상태가_종료가_아닌경우_예외가_발생한다() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("1인 메뉴"));
            final ProductResponse product = productService.create(new ProductCreateRequest("짜장면", 1000));
            final MenuResponse menu = menuService.create(new MenuCreateRequest("짜장면", BigDecimal.valueOf(1000), menuGroup.getId(),
                List.of(new MenuProductCreateRequest(product.getId(), 1))));
            final OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));
            final OrderTable orderTable2 = tableService.create(new OrderTable(null, 3, true));
            final TableGroup tableGroup = tableGroupService.create(
                new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));
            orderService.create(
                new Order(orderTable1.getId(), LocalDateTime.now(), List.of(new OrderLineItem(menu.getId(), 1))));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}