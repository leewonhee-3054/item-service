package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    // 전체 목록 보여주기
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    // 해당 ID의 물품 상세정보 보여주기
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    // 단순히 추가하는 창 띄우기
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    // 단순히 삭제하는 창 띄우기
    @GetMapping("{itemId}/delete")
    public String deleteForm(@PathVariable Long itemId) {
        return "basic/DeleteForm";
    }


    @GetMapping("/result-delete")
    public String deleteResult() {
        return "basic/DeleteResult";
    }


    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {
        Item item = new Item(itemName, price, quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);
        return "basic/item";
    }

    //@PostMapping("/add")
    // 이렇게 modelAttribute 옆에 ("item") 해주면 model.addAttribute("item",item) 역할까지 해줌 ㄷㄷ
    // model 도 파라미터로 안받아도 됨
    public String addItemV2(@ModelAttribute("item") Item item) {
        itemRepository.save(item);

        //model.addAttribute("item", item); // 자동 추가. 생략 가능
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        // ("Item") 생략하면
        // 클래스 명 Item -> item 이 모델 attribute 에 담기게 됨
        itemRepository.save(item);
        //model.addAttribute("item", item); // 자동 추가. 생략 가능
        return "basic/item";
    }

    // ModelAttribute 도  생략 가능
    //@PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    //추가 post
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @PostMapping("{itemId}/delete")
    public String deleteFormV1(@PathVariable Long itemId, RedirectAttributes redirectAttributes) {
        Item item = itemRepository.findById(itemId);
        itemRepository.delete(item);
        return "redirect:/basic/items/result-delete";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }


    /*
    * 테스트용 데이터 추가*/
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
