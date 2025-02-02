package net.lopymine.mtd.yacl.custom.option.entry;

import com.google.common.collect.ImmutableSet;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import lombok.experimental.ExtensionMethod;
import net.minecraft.text.Text;
import net.lopymine.mtd.config.other.simple.SimpleEntry;
import net.lopymine.mtd.extension.OptionExtension;
import net.lopymine.mtd.yacl.custom.controller.entry.EntryController;

import java.util.function.*;
import org.jetbrains.annotations.NotNull;

@ExtensionMethod(OptionExtension.class)
public final class EntryOptionImpl<K, V> implements EntryOption<K, V> {

	private final ListOptionEntry<SimpleEntry<K, V>> listOption;
	private final Binding<SimpleEntry<K, V>> entryBinding;
	private final EntryController<K, V> controller;
	private final EntryEntryOption<K> keyOption;
	private final EntryEntryOption<V> valueOption;

	private boolean wrong;
	private Text reason;

	public EntryOptionImpl(EntryController<K, V> controller) {
		this.listOption   = controller.option();
		this.controller   = controller;
		this.entryBinding = new BindingWithNoDefaultValue<>(this.listOption::requestSet, this.listOption::pendingValue);
		this.keyOption    = this.createKeyOption();
		this.valueOption  = this.createValueOption();
	}

	private static <A> EntryEntryOption<A> createOption(Option<?> entryOption, Function<Option<A>, ControllerBuilder<A>> controllerBuilder, Binding<A> optionBinding) {
		return new EntryEntryOption<>() {

			private boolean wrong;
			private Text reason;

			@Override
			public boolean myTotemDoll$wrong() {
				return this.wrong;
			}

			@Override
			public void myTotemDoll$setWrong(boolean bl, Text reason) {
				this.wrong = bl;
				this.reason = reason;

				entryOption.setWrong(bl, reason);
			}

			@Override
			public Text myTotemDoll$getReason() {
				return this.reason;
			}

			@Override
			public @NotNull Text name() {
				return entryOption.name();
			}

			@Override
			public @NotNull OptionDescription description() {
				return entryOption.description();
			}

			@Override
			public @NotNull Text tooltip() {
				return entryOption.tooltip();
			}

			@Override
			public @NotNull Controller<A> controller() {
				return controllerBuilder.apply(this).build();
			}

			@Override
			public @NotNull StateManager<A> stateManager() {
				throw new UnsupportedOperationException("EntryOptionImpl does not support state managers");
			}

			@Override
			public @NotNull Binding<A> binding() {
				return optionBinding;
			}

			@Override
			public boolean available() {
				return entryOption.available();
			}

			@Override
			public void setAvailable(boolean available) {
				// NO-OP
			}

			@Override
			public @NotNull ImmutableSet<OptionFlag> flags() {
				return entryOption.flags();
			}

			@Override
			public boolean changed() {
				return entryOption.changed();
			}

			@Override
			public @NotNull A pendingValue() {
				return optionBinding.getValue();
			}

			@Override
			public void requestSet(@NotNull A value) {
				optionBinding.setValue(value);
			}

			@Override
			public boolean applyValue() {
				return entryOption.applyValue();
			}

			@Override
			public void forgetPendingValue() {
				// NO-OP
			}

			@Override
			public void requestSetDefault() {
				// NO-OP
			}

			@Override
			public boolean isPendingValueDefault() {
				return false;
			}

			@Override
			public void addEventListener(OptionEventListener<A> listener) {
				// NO-OP
			}

			@Override
			public void addListener(BiConsumer<Option<A>, A> changedListener) {
				// NO-OP
			}

			@Override
			public boolean canResetToDefault() {
				return false;
			}
		};
	}



	private EntryEntryOption<K> createKeyOption() {
		Binding<K> generic = new BindingWithNoDefaultValue<>((key) -> this.entryBinding.setValue(this.entryBinding.getValue().setKey(key)), this.entryBinding.getValue()::getKey);
		return EntryOptionImpl.createOption(this, this.controller.getKeyControllerBuilder(), generic);
	}

	private EntryEntryOption<V> createValueOption() {
		Binding<V> generic = new BindingWithNoDefaultValue<>((value) -> this.entryBinding.setValue(this.entryBinding.getValue().setValue(value)), this.entryBinding.getValue()::getValue);
		return EntryOptionImpl.createOption(this, this.controller.getValueControllerBuilder(), generic);
	}

	@Override
	public EntryEntryOption<K> keyOption() {
		return this.keyOption;
	}

	@Override
	public EntryEntryOption<V> valueOption() {
		return this.valueOption;
	}

	@Override
	public @NotNull Text name() {
		return this.listOption.name();
	}

	@Override
	public @NotNull OptionDescription description() {
		return this.listOption.description();
	}

	@Override
	public @NotNull Text tooltip() {
		return this.listOption.tooltip();
	}

	@Override
	public @NotNull Controller<SimpleEntry<K, V>> controller() {
		return this.controller;
	}

	@Override
	public @NotNull StateManager<SimpleEntry<K, V>> stateManager() {
		return StateManager.createSimple(this.binding());
	}

	@Override
	public @NotNull Binding<SimpleEntry<K, V>> binding() {
		return this.entryBinding;
	}

	@Override
	public void setAvailable(boolean available) {
		this.listOption.available();
	}

	@Override
	public boolean changed() {
		return false;
	}

	@NotNull
	@Override
	public SimpleEntry<K, V> pendingValue() {
		return this.listOption.pendingValue();
	}

	@Override
	public void requestSet(@NotNull SimpleEntry<K, V> value) {
		this.listOption.requestSet(value);
	}

	@Override
	public boolean applyValue() {
		return this.listOption.applyValue();
	}

	@Override
	public void forgetPendingValue() {
		this.listOption.forgetPendingValue();
	}

	@Override
	public void requestSetDefault() {
		this.listOption.requestSetDefault();
	}

	@Override
	public boolean isPendingValueDefault() {
		return this.listOption.isPendingValueDefault();
	}

	@Override
	public void addEventListener(OptionEventListener<SimpleEntry<K, V>> listener) {
		this.listOption.addEventListener(listener);
	}

	@Override
	public void addListener(BiConsumer<Option<SimpleEntry<K, V>>, SimpleEntry<K, V>> changedListener) {
		this.listOption.addListener(changedListener);
	}

	@Override
	public void myTotemDoll$setWrong(boolean bl, Text reason) {
		this.wrong = bl;
		this.reason = reason;
		this.listOption.setWrong(bl, reason);
	}

	@Override
	public boolean myTotemDoll$wrong() {
		return this.wrong;
	}

	@Override
	public Text myTotemDoll$getReason() {
		return this.reason;
	}

	@Override
	public ListOption<SimpleEntry<K, V>> parentGroup() {
		return this.listOption.parentGroup();
	}

	public static class BindingWithNoDefaultValue<A> implements Binding<A> {

		private final Consumer<A> setter;
		private final Supplier<A> getter;

		public BindingWithNoDefaultValue(Consumer<A> setter, Supplier<A> getter) {
			this.setter = setter;
			this.getter = getter;
		}

		@Override
		public A getValue() {
			return this.getter.get();
		}

		@Override
		public void setValue(A value) {
			this.setter.accept(value);
		}

		@Override
		public A defaultValue() {
			throw new UnsupportedOperationException();
		}
	}
}