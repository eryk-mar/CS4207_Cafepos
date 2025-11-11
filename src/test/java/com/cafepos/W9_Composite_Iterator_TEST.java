package com.cafepos;
import com.cafepos.menu.*;
import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
public class W9_Composite_Iterator_TEST {
    @Test
    void compositeIterator_DepthFirst_WithYourStructure() {
        // Note: the depth labelling is in the D{num} format to inform the reader of the three structure
        Menu root = new Menu("ROOT_D0");
        Menu menuA = new Menu("MenuA_D1");
        Menu menuB = new Menu("MenuB_D1");

        MenuItem vegSamosasD2 = new MenuItem("VegSamosas_D2", Money.of(7.0), true);
        MenuItem fish_n_chipsD2 = new MenuItem("FishAndChips_D2", Money.of(12), false);
        MenuItem jalepeno_poppersD2 = new MenuItem("JalapenoPoppers_D2", Money.of(8), true);
        MenuItem ribeyeD2 = new MenuItem("RibEye_D2", Money.of(25.0), false);
        MenuItem cauliflower_wingsD2 = new MenuItem("CauliflowerWings_D2", Money.of(6.5), true);

        // Root 1st - >
        // MenuA 2nd, MenuB 5th -->
        // [vegsamosa 3rd, fishnchips 4th] [jalapenopoppers 6th, ribeye 7th, cauliflowerwings 8th]
        root.add(menuA);
        root.add(menuB);
        menuA.add(vegSamosasD2);
        menuA.add(fish_n_chipsD2);

        menuB.add(jalepeno_poppersD2);
        menuB.add(ribeyeD2);
        menuB.add(cauliflower_wingsD2);

        // string below records the traversal
        List<String> traversalOrder = new ArrayList<>();
        Iterator<MenuComponent> iterator = root.iterator();
        while (iterator.hasNext()) {
            traversalOrder.add(iterator.next().name());
        }

        // verification for proper DFS order (the index is -2 to the order in which the nodes branch and does not include the root)
        assertEquals("MenuA_D1", traversalOrder.get(0)); // Root's first child
        assertEquals("VegSamosas_D2", traversalOrder.get(1));  // MenuA's first child
        assertEquals("FishAndChips_D2", traversalOrder.get(2)); // MenuA's second child
        assertEquals("MenuB_D1", traversalOrder.get(3));        // Root's second child
        assertEquals("JalapenoPoppers_D2", traversalOrder.get(4)); // MenuB's first child
        assertEquals("RibEye_D2", traversalOrder.get(5));       // MenuB's second child
        assertEquals("CauliflowerWings_D2", traversalOrder.get(6)); // MenuB's third child

    }
}
