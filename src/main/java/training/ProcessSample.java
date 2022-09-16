package training;

public class ProcessSample {

    public static void main(String[] args) {
        ProcessHandle
                .allProcesses()
                .filter(process -> process.info().command().isPresent() &&
                        process.info().command().get().toLowerCase().contains("notepad"))
                .forEach(p -> p.destroy());
    }
}
