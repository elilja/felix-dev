/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.felix.karaf.gshell.console.commands;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.io.PrintStream;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.basic.AbstractCommand;
import org.apache.felix.gogo.commands.basic.ActionPreparator;
import org.apache.felix.gogo.commands.basic.DefaultActionPreparator;
import org.apache.felix.karaf.gshell.console.BlueprintContainerAware;
import org.apache.felix.karaf.gshell.console.BundleContextAware;
import org.apache.felix.karaf.gshell.console.CompletableFunction;
import org.apache.felix.karaf.gshell.console.Completer;
import org.osgi.framework.BundleContext;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.osgi.service.blueprint.container.Converter;
import org.osgi.service.command.CommandSession;
import org.fusesource.jansi.Ansi;

public class BlueprintCommand extends AbstractCommand implements CompletableFunction
{

    protected BlueprintContainer blueprintContainer;
    protected Converter blueprintConverter;
    protected String actionId;
    protected List<Completer> completers;

    public void setBlueprintContainer(BlueprintContainer blueprintContainer) {
        this.blueprintContainer = blueprintContainer;
    }

    public void setBlueprintConverter(Converter blueprintConverter) {
        this.blueprintConverter = blueprintConverter;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public List<Completer> getCompleters() {
        return completers;
    }

    public void setCompleters(List<Completer> completers) {
        this.completers = completers;
    }

    @Override
    protected ActionPreparator getPreparator() throws Exception {
        return new BlueprintActionPreparator();
    }

    class BlueprintActionPreparator extends DefaultActionPreparator {

        @Override
        protected Object convert(Action action, CommandSession commandSession, Object o, Type type) throws Exception {
            return blueprintConverter.convert(o, new GenericType(type));
        }

        @Override
        protected void printUsage(Command command, Set<Option> options, Set<Argument> arguments, PrintStream out)
        {
            options = new HashSet<Option>(options);
            options.add(HELP);
            if (command != null && (command.description() != null) || command.name() != null)
            {
                out.println(Ansi.ansi().a(Ansi.Attribute.INTENSITY_BOLD).a("DESCRIPTION").a(Ansi.Attribute.RESET));
                out.print("\t");
                if (command.name() != null) {
                    out.println(Ansi.ansi().a(command.scope()).a(":").a(Ansi.Attribute.INTENSITY_BOLD).a(command.name()).a(Ansi.Attribute.RESET));
                    out.println();
                }
                out.print("\t");
                out.println(command.description());
                out.println();
            }
            String syntax = "";
            if (command != null)
            {
                syntax += command.scope() + ":" + command.name();
            }
            if (options.size() > 0)
            {
                syntax += " [options]";
            }
            if (arguments.size() > 0)
            {
                if (arguments.size() > 0) {
                    syntax += " ";
                }
                for (Argument argument : arguments)
                {
                    if (argument.required())
                    {
                        syntax += "[" + argument.name() + "]";
                    }
                    else
                    {
                        syntax += argument.name();
                    }
                }
            }

            out.println(Ansi.ansi().a(Ansi.Attribute.INTENSITY_BOLD).a("SYNTAX").a(Ansi.Attribute.RESET));
            out.print("\t");
            out.println(syntax);
            out.println();
            if (arguments.size() > 0)
            {
                out.println(Ansi.ansi().a(Ansi.Attribute.INTENSITY_BOLD).a("ARGUMENTS").a(Ansi.Attribute.RESET));
                for (Argument argument : arguments)
                {
                    out.print("\t");
                    out.print(argument.name());
                    out.print("\t");
                    out.print(argument.description());
                    out.println();
                }
                out.println();
            }
            if (options.size() > 0)
            {
                out.println(Ansi.ansi().a(Ansi.Attribute.INTENSITY_BOLD).a("OPTIONS").a(Ansi.Attribute.RESET));
                for (Option option : options)
                {
                    String opt = option.name();
                    for (String alias : option.aliases())
                    {
                        opt += ", " + alias;
                    }
                    out.print("\t");
                    out.println(opt);
                    out.print("\t\t");
                    out.print(option.description());
                    out.println();
                }
                out.println();
            }
        }

        protected void printFormatted(String prefix, String str, int termWidth, PrintStream out) {
            int pfxLen = length(prefix);
            
        }

        protected int length(String str) {
            return str.length();
        }

    }

    protected Action createNewAction() throws Exception {
        Action action = (Action) blueprintContainer.getComponentInstance(actionId);
        if (action instanceof BlueprintContainerAware) {
            ((BlueprintContainerAware) action).setBlueprintContainer(blueprintContainer);
        }
        if (action instanceof BundleContextAware) {
            BundleContext context = (BundleContext) blueprintContainer.getComponentInstance("blueprintBundleContext");
            ((BundleContextAware) action).setBundleContext(context);
        }
        return action;
    }

}
