package me.gavin.calypso.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.calypso.misc.RenderUtil;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;

public class Panel extends DragComponent implements Typeable {

    private final ModCategory category;
    private final Minecraft mc;

    private boolean open = true;

    private final ArrayList<Button> buttons;
    private final Rect toggleButton;
    private final Rect panelRect;

    public Panel(ModCategory category, ArrayList<Module> modules, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.category = category;
        this.mc = Minecraft.getMinecraft();
        this.buttons = new ArrayList<>();
        this.toggleButton = new Rect(x + width - 11, y + 1, 10, 10);
        this.panelRect = new Rect(x, y, width, height);

        for (Module module : modules) {
            buttons.add(new Button(module, x, y, width - 4, height));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isWithin(mouseX, mouseY) && !toggleButton.isWithin(mouseX, mouseY) && mouseButton == 0) {
            startDragLogic(mouseX, mouseY);
        }

        if (toggleButton.isWithin(mouseX, mouseY) && mouseButton == 0)
            open = !open;


        if (open) {
            for (Button button : buttons) {
                button.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            stopDragLogic(mouseX, mouseY);

        if (open) {
            for (Button button : buttons) {
                button.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        updateDragPos(mouseX, mouseY);
        this.toggleButton.x = x + width - 10;
        this.toggleButton.y = y + 1;
        this.panelRect.setX(x);
        this.panelRect.setY(y);

        float rectHeight = height;
        if (open) {
            // rendering background (will make a better way later idk)

            rectHeight += 1f;
            for (Button button : buttons) {
                rectHeight += button.getTotalHeight();
            }
            Gui.drawRect(x, y, x + width, (int) (y + rectHeight), 0x80060606);
        }
        this.panelRect.height = (int) rectHeight;

        GlStateManager.glLineWidth(1.5f);
        RenderUtil.draw2dRect(x, y, x + width, y + rectHeight + 0.25f, 0xFFBF0000);

        Gui.drawRect(x, y, x + width, y + height, 0xFFBF0000);
        GlStateManager.pushMatrix();
        GlStateManager.translate(
                x + (width / 2f) - ((mc.fontRenderer.getStringWidth(category.name()) / 2f) * 1.1),
                y + (height / 2f) - ((mc.fontRenderer.FONT_HEIGHT / 2f) * 1.1), 0);
        GlStateManager.scale(1.1, 1.1, 0);
        mc.fontRenderer.drawString(category.name(), 0, 0, -1, true);
        GlStateManager.popMatrix();

        final ChatFormatting color = this.toggleButton.isWithin(mouseX, mouseY) ? ChatFormatting.GRAY : ChatFormatting.WHITE;
        final String sign = open ? "-" : "+";
        mc.fontRenderer.drawStringWithShadow(color + sign,
                toggleButton.x + (toggleButton.width / 2f) - (mc.fontRenderer.getStringWidth(sign) / 2f),
                toggleButton.y + (toggleButton.height / 2f) - (mc.fontRenderer.FONT_HEIGHT / 2f) + 0.5f, -1);



        if (open) {
            // rendering buttons
            int yoffset = height + 1;
            for (Button button : buttons) {
                button.x = x + 2;
                button.y = y + yoffset;

                yoffset += button.getTotalHeight();

                button.draw(mouseX, mouseY, partialTicks);
            }
        }
    }

    public ModCategory getCategory() {
        return category;
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    @Override
    public void keyTyped(char keyChar, int keycode) {
        if (open) {
            for (Button button : buttons) {
                button.keyTyped(keyChar, keycode);
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Rect getPanelRect() {
        return panelRect;
    }

}