import puppeteer from "puppeteer";

describe("Booking Form", () => {
  const app = "http://localhost:3000/";
  let browser: puppeteer.Browser;
  let page:puppeteer.Page;

  beforeEach(async () => {
     browser = await puppeteer.launch({
      headless: false,
      slowMo: 20,
    });
     page = await browser.newPage();
    await page.goto(app);
    await page.waitForSelector("#root");
  },9000000);

  test("add", async () => {
    await page.waitForSelector(".booking_btn")
    await page.click(".booking_btn");
    await page.waitForSelector(".MuiStepper-root");
    await page.click("div[test-id=Duration]");
    await page.waitForSelector('li[role="option"]');
    await page.click('li[role="option"][data-value="Half-day Morning"]');
    await page.type("input[id=attendees]", "10");
    await page.click("#next_btn");
    await page.waitForSelector(".MuiToggleButton-label");
    await page.click("span.MuiToggleButton-label");
    await page.click('input[type="checkbox"]');
    await page.click("#next_btn");
    await page.waitFor(250);
    await page.click("#next_btn");
    await page.type("input[id=Title]", "Mr.");
    await page.type("input[id=phoneNo]", "9999999999");
    await page.type("input[id=address]", "4/23-a");
    await page.type("input[id=city]", "Bangalore");
    await page.type("input[id=zip]", "123456");
    await page.type("input[id=state]", "Karnataka");
    await page.type("input[id=country]", "India");
    await page.click("#next_btn");
    await page.waitFor(250);
    await page.click("#confirm_btn");
    await page.waitForSelector(".booking_btn");
    const url = page.url();
    expect(url).toBe(app);
  }, 9000000);
  afterAll(async () => {
    browser.close();
  });
});
