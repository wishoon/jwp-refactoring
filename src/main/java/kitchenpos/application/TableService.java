package kitchenpos.application;

import static kitchenpos.domain.table.TableStatus.READY;

import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(null, request.getNumberOfGuests(), request.isEmpty(),
            READY);
        return OrderTableResponse.createResponse(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
            .stream()
            .map(OrderTableResponse::createResponse)
            .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableChangeEmptyRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        orderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.createResponse(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.createResponse(orderTable);
    }
}
