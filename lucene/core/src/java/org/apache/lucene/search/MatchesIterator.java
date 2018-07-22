/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.search;

import java.io.IOException;

import org.apache.lucene.index.LeafReaderContext;

/**
 * An iterator over match positions (and optionally offsets) for a single document and field
 *
 * To iterate over the matches, call {@link #next()} until it returns {@code false}, retrieving
 * positions and/or offsets after each call.  You should not call the position or offset methods
 * before {@link #next()} has been called, or after {@link #next()} has returned {@code false}.
 *
 * Matches from some queries may span multiple positions.  You can retrieve the positions of
 * individual matching terms on the current match by calling {@link #getSubMatches()}.
 *
 * Matches are ordered by start position, and then by end position.  Match intervals may overlap.
 *
 * @see Weight#matches(LeafReaderContext, int)
 *
 * @lucene.experimental
 */
public interface MatchesIterator {

  /**
   * Advance the iterator to the next match position
   * @return {@code true} if matches have not been exhausted
   */
  boolean next() throws IOException;

  /**
   * The start position of the current match
   *
   * Should only be called after {@link #next()} has returned {@code true}
   */
  int startPosition();

  /**
   * The end position of the current match
   *
   * Should only be called after {@link #next()} has returned {@code true}
   */
  int endPosition();

  /**
   * The starting offset of the current match, or {@code -1} if offsets are not available
   *
   * Should only be called after {@link #next()} has returned {@code true}
   */
  int startOffset() throws IOException;

  /**
   * The ending offset of the current match, or {@code -1} if offsets are not available
   *
   * Should only be called after {@link #next()} has returned {@code true}
   */
  int endOffset() throws IOException;

  /**
   * Returns a MatchesIterator that iterates over the positions and offsets of individual
   * terms within the current match
   *
   * Should only be called after {@link #next()} has returned {@code true}
   */
  MatchesIterator getSubMatches() throws IOException;

  /**
   * Returns a label identifying the leaf query causing the current match
   *
   * Should only be called after {@link #next()} has returned {@code true}
   */
  Object label();

  /**
   * A MatchesIterator that is immediately exhausted
   */
  MatchesIterator EMPTY_ITERATOR = new MatchesIterator() {
    @Override
    public boolean next() throws IOException {
      return false;
    }

    @Override
    public int startPosition() {
      throw new UnsupportedOperationException();
    }

    @Override
    public int endPosition() {
      throw new UnsupportedOperationException();
    }

    @Override
    public int startOffset() throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public int endOffset() throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public MatchesIterator getSubMatches() throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public Object label() {
      return this;
    }
  };

}
