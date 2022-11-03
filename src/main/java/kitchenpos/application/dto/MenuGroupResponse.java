package kitchenpos.application.dto;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    protected MenuGroupResponse() {
    }

    public MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse createResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
