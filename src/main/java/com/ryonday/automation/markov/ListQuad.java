/*
Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

This file is part of JMegaHal.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: Quad.java,v 1.3 2004/02/01 13:24:06 pjm2 Exp $

*/

package com.ryonday.automation.markov;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;

public class ListQuad implements java.io.Serializable {

    private final List<String> tokens;

    private boolean canStart = false;
    private boolean canEnd = false;


    public ListQuad(Collection<String> tokens) {

        checkArgument(tokens != null, "Received null token list.");
        checkArgument(tokens.size() == 4, "Received undersized token list: %s", tokens);

        this.tokens = ImmutableList.copyOf( tokens );
    }

    public String getToken(int index) {
        checkArgument(index >= 0);
        checkArgument(index < tokens.size());

        return tokens.get( index );
    }

    public List<String> getTokens() {
        return this.tokens;
    }

    public ListQuad setCanStart(boolean flag) {
        canStart = flag;
        return this;
    }

    public ListQuad setCanEnd(boolean flag) {
        canEnd = flag;
        return this;
    }

    public boolean canStart() {
        return canStart;
    }

    public boolean canEnd() {
        return canEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListQuad)) return false;
        ListQuad listQuad = (ListQuad) o;
        return Objects.equals(tokens, listQuad.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokens);
    }

    @Override
    public String toString() {
        return toStringHelper(ListQuad.class)
                .add("tokens", tokens)
                .add("canEnd", canEnd)
                .add("canStart", canStart)
                .toString();
    }
}
