package lt.code.samples.maven.worklog.controller;

import lombok.RequiredArgsConstructor;
import lt.code.samples.maven.order.model.OrderEntity;
import lt.code.samples.maven.worklog.dto.FaultRequestDto;
import lt.code.samples.maven.worklog.dto.StartWorkRequestDto;
import lt.code.samples.maven.worklog.service.WorkLogEndService;
import lt.code.samples.maven.worklog.service.WorkLogFaultService;
import lt.code.samples.maven.worklog.service.WorkLogFixService;
import lt.code.samples.maven.worklog.service.WorkLogStartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders/worklog")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class WorkLogActionsController {

    private final WorkLogStartService workLogStartService;
    private final WorkLogEndService workLogEndService;
    private final WorkLogFaultService workLogFaultService;
    private final WorkLogFixService workLogFixService;

    @PostMapping("/start")
    public String start(@RequestParam Long orderId,
                        @RequestParam String operation,
                        RedirectAttributes ra) {
        StartWorkRequestDto req = new StartWorkRequestDto(orderId, operation);
        OrderEntity order = workLogStartService.startWork(req.orderId(), null, req.operation());
        ra.addFlashAttribute("message", "Darbas pradėtas.");
        return "redirect:/orders/worklog?orderId=" + order.getId();
    }

    @PostMapping("/end")
    public String end(@RequestParam Long workLogId,
                      @RequestParam Long orderId,
                      RedirectAttributes ra) {
        var res = workLogEndService.endWork(workLogId);
        if (res.isSuccess()) {
            ra.addFlashAttribute("message", "Darbas užbaigtas.");
        } else {
            ra.addFlashAttribute("error", res.getMessage());
        }
        return "redirect:/orders/worklog?orderId=" + orderId;
    }

    @PostMapping("/fault")
    public String markFault(@RequestParam Long workLogId,
                            @RequestParam String operationName,
                            @RequestParam(required = false) String faultDescription,
                            @RequestParam Long orderId,
                            RedirectAttributes ra) {
        FaultRequestDto req = new FaultRequestDto(operationName, faultDescription);
        workLogFaultService.markAsFaultyAndGetOrderName(workLogId, req.operationName(), req.faultDescription());
        ra.addFlashAttribute("message", "Pažymėta kaip brokas.");
        return "redirect:/orders/worklog?orderId=" + orderId;
    }

    @PostMapping("/fix/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String fix(@PathVariable Long id,
                      @RequestParam Long orderId,
                      RedirectAttributes ra) {
        boolean fixed = workLogFixService.fixFault(id);
        if (fixed) {
            ra.addFlashAttribute("message", "Brokas sutvarkytas.");
        } else {
            ra.addFlashAttribute("error", "Įrašas nebuvo pažymėtas kaip brokuotas.");
        }
        return "redirect:/orders/worklog?orderId=" + orderId;
    }
}
