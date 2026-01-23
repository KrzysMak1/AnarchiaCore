package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XModule;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Sound;
import java.util.EnumMap;
import java.util.HashMap;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.bukkit.Location;
import java.util.function.Supplier;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.bukkit.Note;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Instrument;
import java.util.Map;

public final class NoteBlockMusic
{
    private static final Map<String, Instrument> INSTRUMENTS;
    private static final Map<Instrument, XSound> INSTRUMENT_TO_SOUND;
    
    private NoteBlockMusic() {
    }
    
    @NotNull
    public static XSound getSoundFromInstrument(@NotNull final Instrument instrument) {
        return (XSound)NoteBlockMusic.INSTRUMENT_TO_SOUND.get((Object)instrument);
    }
    
    @Nullable
    public static Note.Tone getNoteTone(final char ch) {
        switch (ch) {
            case 'A': {
                return Note.Tone.A;
            }
            case 'B': {
                return Note.Tone.B;
            }
            case 'C': {
                return Note.Tone.C;
            }
            case 'D': {
                return Note.Tone.D;
            }
            case 'E': {
                return Note.Tone.E;
            }
            case 'F': {
                return Note.Tone.F;
            }
            case 'G': {
                return Note.Tone.G;
            }
            default: {
                return null;
            }
        }
    }
    
    public static void testMusic(@NotNull final Player player) {
        Objects.requireNonNull((Object)player);
        playMusic(player, (Supplier<Location>)player::getLocation, "PIANO,D,2,100 PIANO,B#1 200 PIANO,F 250 PIANO,E 250 PIANO,B 200 PIANO,A 100 PIANO,B 100 PIANO,E");
    }
    
    public static void fromFile(@NotNull final Player player, @NotNull final Supplier<Location> location, @NotNull final Path path) {
        try (final BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    parseInstructions((CharSequence)line).play(player, location, true);
                }
            }
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void playMusic(@NotNull final Player player, @NotNull final Supplier<Location> location, @Nullable final String script) {
        if (Strings.isNullOrEmpty(script)) {
            return;
        }
        final Sequence seq = parseInstructions((CharSequence)script);
        seq.play(player, location, true);
    }
    
    public static Sequence parseInstructions(@NotNull final CharSequence script) {
        return new InstructionBuilder(script).sequence;
    }
    
    private static void sleep(final long fermata) {
        try {
            Thread.sleep(fermata);
        }
        catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Nullable
    public static Note parseNote(@NotNull final String note) {
        final Note.Tone tone = getNoteTone((char)(note.charAt(0) & '_'));
        if (tone == null) {
            return null;
        }
        final int len = note.length();
        char toneType = ' ';
        int octave = 0;
        if (len > 1) {
            toneType = note.charAt(1);
            if (isDigit(toneType)) {
                octave = toneType - '0';
            }
            else if (len > 2) {
                final char octaveDigit = note.charAt(2);
                if (isDigit(octaveDigit)) {
                    octave = octaveDigit - '0';
                }
            }
            if (octave < 0 || octave > 2) {
                octave = 0;
            }
        }
        return (toneType == '#') ? Note.sharp(octave, tone) : ((toneType == '_') ? Note.flat(octave, tone) : Note.natural(octave, tone));
    }
    
    private static boolean isDigit(final char ch) {
        return ch >= '0' && ch <= '9';
    }
    
    public static float noteToPitch(@NotNull final Note note) {
        return (float)Math.pow(2.0, (note.getId() - 12.0) / 12.0);
    }
    
    @NotNull
    public static BukkitTask playAscendingNote(@NotNull final Plugin plugin, @NotNull final Player player, @NotNull final Entity playTo, @NotNull final Instrument instrument, final int ascendLevel, final int delay) {
        Objects.requireNonNull((Object)player, "Cannot play note from null player");
        Objects.requireNonNull((Object)playTo, "Cannot play note to null entity");
        if (ascendLevel <= 0) {
            throw new IllegalArgumentException("Note ascend level cannot be lower than 1");
        }
        if (ascendLevel > 7) {
            throw new IllegalArgumentException("Note ascend level cannot be greater than 7");
        }
        if (delay <= 0) {
            throw new IllegalArgumentException("Delay ticks must be at least 1");
        }
        return new BukkitRunnable() {
            int repeating = ascendLevel;
            
            public void run() {
                player.playNote(playTo.getLocation(), instrument, Note.natural(1, Note.Tone.values()[ascendLevel - this.repeating]));
                if (this.repeating-- == 0) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, (long)delay);
    }
    
    static {
        INSTRUMENTS = (Map)new HashMap(50);
        (INSTRUMENT_TO_SOUND = (Map)new EnumMap((Class)Instrument.class)).put((Object)Instrument.PIANO, (Object)XSound.BLOCK_NOTE_BLOCK_HARP);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.BASS_DRUM, (Object)XSound.BLOCK_NOTE_BLOCK_BASEDRUM);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.SNARE_DRUM, (Object)XSound.BLOCK_NOTE_BLOCK_SNARE);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.STICKS, (Object)XSound.BLOCK_NOTE_BLOCK_HAT);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.BASS_GUITAR, (Object)XSound.BLOCK_NOTE_BLOCK_BASS);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.FLUTE, (Object)XSound.BLOCK_NOTE_BLOCK_FLUTE);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.BELL, (Object)XSound.BLOCK_NOTE_BLOCK_BELL);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.GUITAR, (Object)XSound.BLOCK_NOTE_BLOCK_GUITAR);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.CHIME, (Object)XSound.BLOCK_NOTE_BLOCK_CHIME);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.XYLOPHONE, (Object)XSound.BLOCK_NOTE_BLOCK_XYLOPHONE);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.IRON_XYLOPHONE, (Object)XSound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.COW_BELL, (Object)XSound.BLOCK_NOTE_BLOCK_COW_BELL);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.DIDGERIDOO, (Object)XSound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.BIT, (Object)XSound.BLOCK_NOTE_BLOCK_BIT);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.BANJO, (Object)XSound.BLOCK_NOTE_BLOCK_BANJO);
        NoteBlockMusic.INSTRUMENT_TO_SOUND.put((Object)Instrument.PLING, (Object)XSound.BLOCK_NOTE_BLOCK_PLING);
        NoteBlockMusic.INSTRUMENTS.put((Object)"HARP", (Object)Instrument.PIANO);
        NoteBlockMusic.INSTRUMENTS.put((Object)"BASEDRUM", (Object)Instrument.BASS_DRUM);
        NoteBlockMusic.INSTRUMENTS.put((Object)"BASE_DRUM", (Object)Instrument.BASS_DRUM);
        NoteBlockMusic.INSTRUMENTS.put((Object)"SNARE", (Object)Instrument.SNARE_DRUM);
        NoteBlockMusic.INSTRUMENTS.put((Object)"BASS", (Object)Instrument.BASS_GUITAR);
        NoteBlockMusic.INSTRUMENTS.put((Object)"COWBELL", (Object)Instrument.COW_BELL);
        for (final Instrument instrument : Instrument.values()) {
            final String name = instrument.name();
            NoteBlockMusic.INSTRUMENTS.put((Object)name, (Object)instrument);
            final StringBuilder alias = new StringBuilder(String.valueOf(name.charAt(0)));
            final int index = name.indexOf(95);
            if (index != -1) {
                alias.append(name.charAt(index + 1));
            }
            if (NoteBlockMusic.INSTRUMENTS.putIfAbsent((Object)alias.toString(), (Object)instrument) != null) {
                for (int i = 0; i < name.length(); ++i) {
                    final char ch = name.charAt(i);
                    if (ch == '_') {
                        ++i;
                    }
                    else {
                        alias.append(ch);
                        if (NoteBlockMusic.INSTRUMENTS.putIfAbsent((Object)alias.toString(), (Object)instrument) == null) {
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private enum InstructionParserPhase
    {
        NEUTRAL {
            @Override
            protected InstructionParserPhase next() {
                return NoteBlockMusic$InstructionParserPhase$1.INSTRUMENT;
            }
            
            @Override
            protected char checkup(final char ch) {
                throw new AssertionError((Object)"Checkup should not be performed on NEUTRAL instruction parser phase");
            }
        }, 
        INSTRUMENT {
            @Override
            protected InstructionParserPhase next() {
                return NoteBlockMusic$InstructionParserPhase$2.NOTE;
            }
            
            @Override
            protected char checkup(final char ch) {
                if (ch >= 'a' && ch <= 'z') {
                    return (char)(ch & '_');
                }
                return ((ch >= 'A' && ch <= 'Z') || ch == '_' || ch == '-') ? ch : '\0';
            }
        }, 
        NOTE {
            @Override
            protected InstructionParserPhase next() {
                return NoteBlockMusic$InstructionParserPhase$3.RESTATEMENT;
            }
            
            @Override
            protected char checkup(final char ch) {
                if (ch >= 'a' && ch <= 'z') {
                    return (char)(ch & '_');
                }
                return ((ch >= 'A' && ch <= 'Z') || isDigit(ch) || ch == '.' || ch == '_' || ch == '#') ? ch : '\0';
            }
        }, 
        END_SEQ {
            @Override
            protected InstructionParserPhase next() {
                return NoteBlockMusic$InstructionParserPhase$4.RESTATEMENT;
            }
            
            @Override
            protected char checkup(final char ch) {
                return '\0';
            }
        }, 
        RESTATEMENT {
            @Override
            protected InstructionParserPhase next() {
                return NoteBlockMusic$InstructionParserPhase$5.RESTATEMENT_DELAY;
            }
            
            @Override
            protected char checkup(final char ch) {
                return isDigit(ch) ? ch : '\0';
            }
        }, 
        RESTATEMENT_DELAY {
            @Override
            protected InstructionParserPhase next() {
                return NoteBlockMusic$InstructionParserPhase$6.FERMATA;
            }
            
            @Override
            protected char checkup(final char ch) {
                return isDigit(ch) ? ch : '\0';
            }
        }, 
        FERMATA {
            @Override
            protected InstructionParserPhase next() {
                return NoteBlockMusic$InstructionParserPhase$7.NEUTRAL;
            }
            
            @Override
            protected char checkup(final char ch) {
                return isDigit(ch) ? ch : '\0';
            }
        };
        
        protected abstract InstructionParserPhase next();
        
        protected abstract char checkup(final char p0);
    }
    
    private static final class InstructionBuilder
    {
        @NotNull
        final CharSequence script;
        final int len;
        final StringBuilder instrumentBuilder;
        final StringBuilder pitchBuiler;
        final StringBuilder volumeBuilder;
        final StringBuilder restatementBuilder;
        final StringBuilder restatementDelayBuilder;
        final StringBuilder fermataBuilder;
        int i;
        boolean isSequence;
        boolean isBuilding;
        Sequence sequence;
        InstructionParserPhase phase;
        StringBuilder currentBuilder;
        
        public InstructionBuilder(@NotNull final CharSequence script) {
            this.instrumentBuilder = new StringBuilder(10);
            this.pitchBuiler = new StringBuilder(3);
            this.volumeBuilder = new StringBuilder(3);
            this.restatementBuilder = new StringBuilder(10);
            this.restatementDelayBuilder = new StringBuilder(10);
            this.fermataBuilder = new StringBuilder(10);
            this.sequence = new Sequence();
            this.phase = InstructionParserPhase.NEUTRAL;
            this.script = script;
            this.len = script.length();
            while (this.i < this.len) {
                char ch = script.charAt(this.i);
                switch (ch) {
                    case '(': {
                        final Sequence parent = new Sequence();
                        parent.parent = this.sequence;
                        this.sequence = parent;
                        break;
                    }
                    case ')': {
                        if (this.sequence.parent == null) {
                            this.err("Cannot find start of the sequence for sequence at: " + this.i);
                        }
                        this.buildAndAddInstruction();
                        this.sequence = this.sequence.parent;
                        this.prepareHandlers();
                        this.phase = InstructionParserPhase.END_SEQ;
                        this.isSequence = true;
                        break;
                    }
                    case ' ': {
                        if (!this.isBuilding) {
                            break;
                        }
                        this.isBuilding = false;
                        switch (this.phase.ordinal()) {
                            case 6: {
                                this.buildAndAddInstruction();
                                this.prepareHandlers();
                                break;
                            }
                            case 2:
                            case 5: {
                                this.phase = InstructionParserPhase.FERMATA;
                                this.currentBuilder = this.fermataBuilder;
                                break;
                            }
                        }
                        break;
                    }
                    case ':': {
                        if (this.phase == InstructionParserPhase.NOTE) {
                            this.currentBuilder = this.volumeBuilder;
                            break;
                        }
                        this.err("Unexpected ':' pitch-volume separator at " + this.i + " with current phase: " + (Object)this.phase);
                        break;
                    }
                    case ',': {
                        switch (this.phase.ordinal()) {
                            case 1: {
                                this.currentBuilder = this.pitchBuiler;
                                break;
                            }
                            case 2:
                            case 3: {
                                this.currentBuilder = this.restatementBuilder;
                                break;
                            }
                            case 4: {
                                this.currentBuilder = this.restatementDelayBuilder;
                                break;
                            }
                            default: {
                                this.err("Unexpected phase '" + (Object)this.phase + "' at index: " + this.i);
                                break;
                            }
                        }
                        this.isBuilding = false;
                        this.phase = this.phase.next();
                        break;
                    }
                    default: {
                        if (this.phase == InstructionParserPhase.NEUTRAL || (this.canBuildInstructionInPhase() && InstructionParserPhase.INSTRUMENT.checkup(ch) != '\0')) {
                            this.currentBuilder = this.instrumentBuilder;
                            if (this.phase == InstructionParserPhase.FERMATA) {
                                this.buildAndAddInstruction();
                                this.prepareHandlers();
                            }
                            this.phase = InstructionParserPhase.INSTRUMENT;
                        }
                        this.isBuilding = true;
                        if ((ch = this.phase.checkup(ch)) == '\0') {
                            this.err("Unexpected char at index " + this.i + " with phase " + (Object)this.phase + ": " + script.charAt(this.i));
                        }
                        this.currentBuilder.append(ch);
                        break;
                    }
                }
                ++this.i;
            }
            this.buildAndAddInstruction();
            this.sequence = this.getRoot();
        }
        
        private Instruction buildInstruction() {
            final int fermata = (this.fermataBuilder.length() == 0) ? 0 : Integer.parseInt(this.fermataBuilder.toString());
            final int restatement = (this.restatementBuilder.length() == 0) ? 1 : Integer.parseInt(this.restatementBuilder.toString());
            final int restatementFermata = (this.restatementDelayBuilder.length() == 0) ? 0 : Integer.parseInt(this.restatementDelayBuilder.toString());
            Instruction instruction;
            if (this.isSequence) {
                instruction = new Sequence(restatement, restatementFermata, fermata);
            }
            else {
                final String instrumentStr = this.instrumentBuilder.toString();
                final Instrument instrument = (Instrument)NoteBlockMusic.INSTRUMENTS.get((Object)instrumentStr);
                XSound sound;
                if (instrument == null) {
                    sound = (XSound)XSound.matchXSound(instrumentStr).orElse((Object)null);
                }
                else {
                    sound = NoteBlockMusic.getSoundFromInstrument(instrument);
                }
                final String pitchStr = this.pitchBuiler.toString();
                final Note note = NoteBlockMusic.parseNote(pitchStr);
                float pitch;
                if (note == null) {
                    pitch = Float.parseFloat(pitchStr);
                }
                else {
                    pitch = NoteBlockMusic.noteToPitch(note);
                }
                float volume = 5.0f;
                if (this.volumeBuilder.length() != 0) {
                    volume = Float.parseFloat(this.volumeBuilder.toString());
                }
                instruction = new Sound(sound, pitch, volume, restatement, restatementFermata, fermata);
            }
            return instruction;
        }
        
        private void prepareHandlers() {
            this.instrumentBuilder.setLength(0);
            this.pitchBuiler.setLength(0);
            this.volumeBuilder.setLength(0);
            this.restatementBuilder.setLength(0);
            this.restatementDelayBuilder.setLength(0);
            this.fermataBuilder.setLength(0);
            this.phase = InstructionParserPhase.NEUTRAL;
            this.isBuilding = false;
            this.isSequence = false;
        }
        
        private boolean canBuildInstructionInPhase() {
            switch (this.phase.ordinal()) {
                case 4:
                case 5:
                case 6: {
                    return true;
                }
                default: {
                    return false;
                }
            }
        }
        
        private void buildAndAddInstruction() {
            this.sequence.addInstruction(this.buildInstruction());
        }
        
        private Sequence getRoot() {
            Sequence sequence;
            for (sequence = this.sequence; sequence.parent != null; sequence = sequence.parent) {}
            return sequence;
        }
        
        private String illustrateError() {
            return '\n' + this.script.toString() + '\n' + Strings.repeat(" ", this.i) + '^';
        }
        
        private void err(final String str) {
            throw new IllegalStateException(str + this.illustrateError());
        }
    }
    
    public static class Sound extends Instruction
    {
        public XSound sound;
        public float volume;
        public float pitch;
        
        public Sound(final Instrument instrument, final Note note, final float volume, final int restatement, final int restatementFermata, final int fermata) {
            super(restatement, restatementFermata, fermata);
            this.sound = NoteBlockMusic.getSoundFromInstrument(instrument);
            this.pitch = NoteBlockMusic.noteToPitch(note);
            this.volume = volume;
        }
        
        public Sound(final XSound sound, final float pitch, final float volume, final int restatement, final int restatementFermata, final int fermata) {
            super(restatement, restatementFermata, fermata);
            this.sound = sound;
            this.pitch = pitch;
            this.volume = volume;
        }
        
        public void setSound(final Instrument instrument) {
            this.sound = NoteBlockMusic.getSoundFromInstrument(instrument);
        }
        
        public void setPitch(final Note note) {
            this.pitch = NoteBlockMusic.noteToPitch(note);
        }
        
        @Override
        public void play(final Player player, final Supplier<Location> location, final boolean playAtLocation) {
            final org.bukkit.Sound bukkitSound = ((XModule<XForm, org.bukkit.Sound>)this.sound).get();
            for (int repeat = this.restatement; repeat > 0; --repeat) {
                final Location finalLocation = (Location)location.get();
                if (bukkitSound != null) {
                    if (playAtLocation) {
                        finalLocation.getWorld().playSound(finalLocation, bukkitSound, this.volume, this.pitch);
                    }
                    else {
                        player.playSound(finalLocation, bukkitSound, this.volume, this.pitch);
                    }
                }
                if (this.restatementFermata > 0) {
                    sleep(this.restatementFermata);
                }
            }
            if (this.fermata > 0) {
                sleep(this.fermata);
            }
        }
        
        @Override
        public String toString() {
            return "Sound:{sound=" + (Object)this.sound + ", pitch=" + this.pitch + ", volume=" + this.volume + ", restatement=" + this.restatement + ", restatementFermata=" + this.restatementFermata + ", fermata=" + this.fermata + '}';
        }
    }
    
    public abstract static class Instruction
    {
        @Nullable
        public Sequence parent;
        public int restatement;
        public int restatementFermata;
        public int fermata;
        
        public Instruction(final int restatement, final int restatementFermata, final int fermata) {
            this.restatement = restatement;
            this.restatementFermata = restatementFermata;
            this.fermata = fermata;
        }
        
        public abstract void play(final Player p0, final Supplier<Location> p1, final boolean p2);
        
        public long getEstimatedLength() {
            return this.restatement * (long)this.restatementFermata;
        }
    }
    
    public static class Sequence extends Instruction
    {
        public Collection<Instruction> instructions;
        
        public Sequence() {
            super(1, 0, 0);
            this.instructions = (Collection<Instruction>)new ArrayList(16);
        }
        
        public Sequence(final Instruction first) {
            super(1, 0, 0);
            (this.instructions = (Collection<Instruction>)new ArrayList(16)).add((Object)first);
        }
        
        public Sequence(final int restatement, final int restatementFermata, final int fermata) {
            super(restatement, restatementFermata, fermata);
            this.instructions = (Collection<Instruction>)new ArrayList(16);
        }
        
        @Override
        public void play(final Player player, final Supplier<Location> location, final boolean playAtLocation) {
            for (int repeat = this.restatement; repeat > 0; --repeat) {
                for (final Instruction instruction : this.instructions) {
                    instruction.play(player, location, playAtLocation);
                }
                if (this.restatementFermata > 0) {
                    sleep(this.restatementFermata);
                }
            }
            if (this.fermata > 0) {
                sleep(this.fermata);
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(200 + this.instructions.size() * 100);
            builder.append("Sequence:{restatement=").append(this.restatement).append(", restatementFermata=").append(this.restatementFermata).append(", fermata=").append(this.fermata).append(", instructions[");
            int i = 0;
            final int size = this.instructions.size();
            for (final Instruction instruction : this.instructions) {
                builder.append((Object)instruction);
                if (++i < size) {
                    builder.append(", ");
                }
            }
            builder.append("]}");
            return builder.toString();
        }
        
        public void addInstruction(final Instruction instruction) {
            instruction.parent = this;
            this.instructions.add((Object)instruction);
        }
        
        @Override
        public long getEstimatedLength() {
            long result = this.restatement * (long)this.restatementFermata;
            for (final Instruction instruction : this.instructions) {
                result += instruction.getEstimatedLength();
            }
            return result;
        }
    }
}
