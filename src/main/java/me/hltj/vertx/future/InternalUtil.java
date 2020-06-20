/*
 * vertx-future-utils - Convenient Utilities for Vert.x Future
 * https://github.com/hltj/vertx-future-utils
 *
 * Copyright (C) 2020  JiaYanwei  https://hltj.me
 *
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Please contact me (jiaywe#at#gmail.com, replace the '#at#' with 'at')
 * if you need additional information or have any questions.
 */
package me.hltj.vertx.future;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class InternalUtil {
    static <T> Supplier<T> toSupplier(Runnable onEmpty, T v0) {
        return () -> {
            onEmpty.run();
            return v0;
        };
    }

    static <T> Function<Throwable, T> toFailureMapper(Consumer<Throwable> onFailure, T v0) {
        return t -> {
            onFailure.accept(t);
            return v0;
        };
    }
}
