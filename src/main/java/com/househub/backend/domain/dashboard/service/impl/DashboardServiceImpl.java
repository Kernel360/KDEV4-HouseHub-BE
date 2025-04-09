package com.househub.backend.domain.dashboard.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.repository.ContractRepository;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.dashboard.dto.DashboardStatsResDto;
import com.househub.backend.domain.dashboard.dto.RecentPropertyResDto;
import com.househub.backend.domain.dashboard.service.DashboardService;
import com.househub.backend.domain.property.repository.PropertyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
	private final PropertyRepository propertyRepository;
	private final ContractRepository contractRepository;
	private final CustomerRepository customerRepository;

	@Override
	public DashboardStatsResDto getDashboardStats(Long agentId) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfMonthLdt = LocalDate.now().withDayOfMonth(1).atStartOfDay();
		LocalDateTime startOfNextMonthLdt = startOfMonthLdt.plusMonths(1);

		LocalDateTime sevenDaysAgo = now.minusDays(7);

		long totalProperties = propertyRepository.countByAgentId(agentId);
		long activeContracts = contractRepository.countByAgentIdAndStatus(agentId, ContractStatus.IN_PROGRESS);
		long newCustomers = customerRepository.countNewCustomersInLast7DaysByAgentId(agentId, sevenDaysAgo);
		long completedContracts = contractRepository.countByAgentIdAndStatusAndCreatedAtBetween(agentId,
			ContractStatus.COMPLETED, startOfMonthLdt,
			startOfNextMonthLdt);

		return DashboardStatsResDto.builder()
			.totalProperties(totalProperties)
			.activeContracts(activeContracts)
			.newCustomers(newCustomers)
			.completedContracts(completedContracts)
			.build();
	}

	@Override
	public List<RecentPropertyResDto> getRecentProperties(int limit) {
		return propertyRepository.findRecentProperties(PageRequest.of(0, limit))
			.stream()
			.map(RecentPropertyResDto::fromEntity)
			.toList();
	}
}
