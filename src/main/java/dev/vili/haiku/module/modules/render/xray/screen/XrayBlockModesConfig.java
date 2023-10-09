package dev.vili.haiku.module.modules.render.xray.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.stream.Stream;

import dev.vili.haiku.module.modules.render.xray.config.BlockConfig;
import dev.vili.haiku.module.modules.render.xray.screen.page.AddPagedButton;
import dev.vili.haiku.module.modules.render.xray.screen.page.AddPagedElement;
import dev.vili.haiku.module.modules.render.xray.screen.page.PagedElement;
import dev.vili.haiku.module.modules.render.xray.screen.page.PagedScreen;
import dev.vili.haiku.module.modules.render.xray.screen.page.RemovePagedButton;
import dev.vili.haiku.module.modules.render.xray.utils.KeyData;
import dev.vili.haiku.module.modules.render.xray.utils.XrayUtils;
import dev.vili.haiku.module.modules.render.xray.view.ViewMode;
import dev.vili.haiku.module.modules.render.xray.widget.BlockConfigWidget;
import dev.vili.haiku.module.modules.render.xray.widget.XrayButton;


public abstract class XrayBlockModesConfig extends PagedScreen<BlockConfig> {
    private class PagedBlockMode extends PagedElement<BlockConfig> {
        private final BlockConfig cfg;
        private boolean textHover = false;
        private BlockConfigWidget blocks;

        public PagedBlockMode(BlockConfig cfg) {
            super(XrayBlockModesConfig.this);
            this.cfg = cfg;
        }

        public PagedBlockMode() {
            this(new BlockConfig());
        }

        @Override
        public void init() {
            int x = width / 2 - 125;
            blocks = addSubWidget(new BlockConfigWidget(x, 0, 125, 20, cfg, XrayBlockModesConfig.this));
            x += 129;
            addSubWidget(XrayButton.builder(KeyData.getName(cfg.getKey()), btn -> {
                client.setScreen(new KeySelector(XrayBlockModesConfig.this, cfg.getKey(), oKey -> {
                    cfg.setKey(oKey);
                    btn.setMessage(KeyData.getName(cfg.getKey()));
                }));
            }).dimensions(x, 0, 56, 20).build());
            x += 60;
            addSubWidget(XrayButton.builder(cfg.getViewMode().getTitle(), btn -> {
                client.setScreen(new EnumSelector<ViewMode>(
                        Text.translatable("x13.mod.mode.view.title"), getParentScreen(), ViewMode.values()) {

                    @Override
                    protected void select(ViewMode element) {
                        cfg.setViewMode(element);
                        btn.setMessage(cfg.getViewMode().getTitle());
                    }

                });
            }).dimensions(x, 0, 64, 20).build());
            x += 68;
            addSubWidget(XrayButton.builder(Text.translatable("x13.mod.template.little"), btn -> client.setScreen(new EnumSelector<>(
                    Text.translatable("x13.mod.template"), XrayBlockModesConfig.this,
                    BlockConfig.Template.values()) {

                @Override
                protected void select(BlockConfig.Template template) {
                    String oldName = cfg.getModeName();
                    int color = cfg.getColor();
                    Optional<KeyData> key = cfg.getKey();
                    template.cloneInto(cfg);
                    cfg.setName(oldName);
                    cfg.setColor(color);
                    cfg.setKey(key);
                }

            })).dimensions(x, 0, 20, 20).build());
            x += 24;

            addSubWidget(new AddPagedButton<>(XrayBlockModesConfig.this,
                    x, 0, 20, 20, PagedBlockMode::new));
            x += 24;
            addSubWidget(new RemovePagedButton(XrayBlockModesConfig.this,
                    x, 0, 20, 20));
            super.init();
        }

        @Override
        public void updateDelta(int delta, int index) {
            blocks.setDeltaY(delta);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            textHover = XrayUtils.isHover(mouseX, mouseY, width / 2 - 200, 0, width / 2 - 125 - 4, 20);
            context.fill(width / 2 - 200, 0, width / 2 - 125 - 4, 20, textHover ? 0x33ffaa00 : 0x33ffffff);
            int w = textRenderer.getWidth(cfg.getModeName());
            context.drawText(textRenderer, cfg.getModeName(), width / 2 - (200 - 125 - 4) / 2 - 125 - 4 - w / 2,
                    10 - textRenderer.fontHeight / 2,
                    cfg.getColor(), false);
            super.render(context, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (textHover) {
                playDownSound();
                client.setScreen(new XrayAbstractModeConfig(XrayBlockModesConfig.this, cfg));
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public BlockConfig save() {
            return cfg;
        }
    }

    public XrayBlockModesConfig(Screen parent, Stream<BlockConfig> stream) {
        super(Text.translatable("x13.mod.mode"), parent, 24, stream);
    }

    @Override
    protected void initElements(Stream<BlockConfig> stream) {
        stream.map(PagedBlockMode::new).forEach(this::addElement);
        addElement(new AddPagedElement<>(this, PagedBlockMode::new));
    }
}
