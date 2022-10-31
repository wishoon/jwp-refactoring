package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest {

    @Nested
    class 메뉴_그룹_생성 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroupRequest menuGroup = new MenuGroupRequest("세트 1번");

            // when
            final MenuGroupResponse extract = menuGroupService.create(menuGroup);

            // then
            assertThat(extract).isNotNull();
        }
    }

    @Nested
    class 메뉴_리스트_조회 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            final MenuGroupRequest menuGroup = new MenuGroupRequest("두마리메뉴");
            menuGroupService.create(menuGroup);

            // when
            final List<MenuGroupResponse> extract = menuGroupService.list();

            // then
            assertThat(extract).hasSize(1);
        }
    }
}